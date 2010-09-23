package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.encrypt.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.utils.EmailUtils;

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

	public Account authenticate(String username, String password) throws UsernameNotFoundException,
			InvalidPasswordException {
		try {
			return jdbcTemplate.queryForObject(passwordProtectedAccountQuery(username), passwordProtectedAccountMapper, username).accessAccount(password, passwordEncoder);
		} catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException(username);
		}
	}

	public void changePassword(Long accountId, String password) {
		jdbcTemplate.update("update Member set password = ? where id = ?", passwordEncoder.encode(password), accountId);
	}

	public Account findById(Long id) {
		return jdbcTemplate.queryForObject(AccountMapper.SELECT_ACCOUNT + " where id = ?", accountMapper, id);
	}

	public Account findByUsername(String username) throws UsernameNotFoundException {
		try {
			return jdbcTemplate.queryForObject(accountQuery(username), accountMapper, username);
		} catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException(username);
		}
	}

	public void markProfilePictureSet(Long id) {
		jdbcTemplate.update("update Member set pictureSet = true where id = ?", id);
	}

	// internal helpers

	private String accountQuery(String username) {
		return EmailUtils.isEmail(username) ? AccountMapper.SELECT_ACCOUNT + " where email = ?" : AccountMapper.SELECT_ACCOUNT + " where username = ?";
	}

	private String passwordProtectedAccountQuery(String username) {
		return EmailUtils.isEmail(username) ? SELECT_PASSWORD_PROTECTED_ACCOUNT + " where email = ?" : SELECT_PASSWORD_PROTECTED_ACCOUNT + " where username = ?";
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