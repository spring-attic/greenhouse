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
package com.springsource.greenhouse.reset;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.SignInNotFoundException;

/**
 * ResetPasswordService implementation that stores reset password requests in a relational database using the JDBC API.
 * Delegates to {@link AccountRepository} to actually change a member's password.
 * Delegates to a {@link ResetPasswordMailer} to send out reset password emails.
 * Delegates to a {@link SecureRandomStringKeyGenerator} to generate unique reset password tokens.
 * @author Keith Donald
 */
@Service
public class JdbcRestPasswordService implements ResetPasswordService {

	private final JdbcTemplate jdbcTemplate;

	private final AccountRepository accountRepository;
	
	private final ResetPasswordMailer mailer;
	
	private final StringKeyGenerator tokenGenerator = KeyGenerators.string();
	
	@Inject
	public JdbcRestPasswordService(JdbcTemplate jdbcTemplate, AccountRepository accountRepository, ResetPasswordMailer mailer) {
		this.jdbcTemplate = jdbcTemplate;
		this.accountRepository = accountRepository;
		this.mailer = mailer;
	}

	@Transactional
	public void sendResetMail(String username) throws SignInNotFoundException {
		Account account = accountRepository.findBySignin(username);
		String token = tokenGenerator.generateKey();
 		jdbcTemplate.update("insert into ResetPassword (token, member) values (?, ?)", token, account.getId());
 		mailer.send(new ResetPasswordRequest(token, account));
	}

	public boolean isValidResetToken(String token) {
		return jdbcTemplate.queryForInt("select count(*) from ResetPassword where token = ?", token) == 1;
	}

	@Transactional
	public void changePassword(String token, String password) throws InvalidResetTokenException {
		Long accountId = findAccountIdByToken(token);
		accountRepository.changePassword(accountId, password);
		jdbcTemplate.update("delete from ResetPassword where token = ?", token);
	}

	// internal helpers
	
	private Long findAccountIdByToken(String token) throws InvalidResetTokenException {
		try {
			return jdbcTemplate.queryForLong("select member from ResetPassword where token = ?", token);       		
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidResetTokenException(token);
		}
	}

}