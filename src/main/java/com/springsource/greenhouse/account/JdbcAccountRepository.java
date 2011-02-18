/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.utils.EmailUtils;

/**
 * AccountRepository implementation that stores Accounts in a relational database using the JDBC API.
 * @author Keith Donald
 */
@Repository
public class JdbcAccountRepository implements AccountRepository {

	private final JdbcTemplate jdbcTemplate;

	private final PasswordEncoder passwordEncoder;

	private final AccountMapper accountMapper;

	@Autowired
	public JdbcAccountRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder, AccountMapper accountMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.passwordEncoder = passwordEncoder;
		this.accountMapper = accountMapper;
	}

	@Transactional
	public Account createAccount(Person person) throws EmailAlreadyOnFileException {
		try {
			jdbcTemplate.update("insert into Member (firstName, lastName, email, password, gender, birthdate) values (?, ?, ?, ?, ?, ?)",
				person.getFirstName(), person.getLastName(), person.getEmail(), passwordEncoder.encode(person.getPassword()), person.getGender().code(), person.getBirthdate().toString());
			Long accountId = jdbcTemplate.queryForLong("call identity()");
			return accountMapper.newAccount(accountId, person);
		} catch (DuplicateKeyException e) {
			throw new EmailAlreadyOnFileException(person.getEmail());
		}
	}

	public Account authenticate(String signin, String password) throws SignInNotFoundException, InvalidPasswordException {
		try {
			return jdbcTemplate.queryForObject(passwordProtectedAccountQuery(signin), passwordProtectedAccountMapper, signin).accessAccount(password, passwordEncoder);
		} catch (EmptyResultDataAccessException e) {
			throw new SignInNotFoundException(signin);
		}
	}

	public void changePassword(Long accountId, String password) {
		jdbcTemplate.update("update Member set password = ? where id = ?", passwordEncoder.encode(password), accountId);
	}

	public Account findById(Long id) {
		return jdbcTemplate.queryForObject(AccountMapper.SELECT_ACCOUNT + " where id = ?", accountMapper, id);
	}

	public List<ProfileReference> findProfileReferencesByIds(List<Long> accountIds) {
		NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> params = new HashMap<String, Object>(2, 1);
		params.put("accountIds", accountIds);
		return namedTemplate.query(AccountMapper.SELECT_ACCOUNT_REFERENCE + " where id in ( :accountIds )", params,
				accountMapper.getReferenceMapper());
	}

	public Account findBySignin(String signin) throws SignInNotFoundException {
		try {
			return jdbcTemplate.queryForObject(accountQuery(signin), accountMapper, signin);
		} catch (EmptyResultDataAccessException e) {
			throw new SignInNotFoundException(signin);
		}
	}

	// internal helpers

	private String accountQuery(String signin) {
		return EmailUtils.isEmail(signin) ? AccountMapper.SELECT_ACCOUNT + " where email = ?" : AccountMapper.SELECT_ACCOUNT + " where username = ?";
	}

	private String passwordProtectedAccountQuery(String signin) {
		return EmailUtils.isEmail(signin) ? SELECT_PASSWORD_PROTECTED_ACCOUNT + " where email = ?" : SELECT_PASSWORD_PROTECTED_ACCOUNT + " where username = ?";
	}

	private RowMapper<PasswordProtectedAccount> passwordProtectedAccountMapper = new RowMapper<PasswordProtectedAccount>() {
		public PasswordProtectedAccount mapRow(ResultSet rs, int row) throws SQLException {
			Account account = accountMapper.mapRow(rs, row);
			return new PasswordProtectedAccount(account, rs.getString("password"));
		}
	};

	private static class PasswordProtectedAccount {

		private Account account;

		private String encodedPassword;

		public PasswordProtectedAccount(Account account, String encodedPassword) {
			this.account = account;
			this.encodedPassword = encodedPassword;
		}

		public Account accessAccount(String password, PasswordEncoder passwordEncoder) throws InvalidPasswordException {
			if (passwordEncoder.matches(password, encodedPassword)) {
				return account;
			} else {
				throw new InvalidPasswordException();
			}
		}

	}

	private static final String SELECT_PASSWORD_PROTECTED_ACCOUNT = "select id, firstName, lastName, email, password, username, gender, pictureSet from Member";
}