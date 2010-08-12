package com.springsource.greenhouse.signup;

import javax.inject.Inject;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.account.Account;

@Service
@Transactional
public class JdbcSignupService implements SignupService {

	private JdbcTemplate jdbcTemplate;
	
	private SignedUpGateway gateway;

	private final PasswordEncoder passwordEncoder;
	
	@Inject
	public JdbcSignupService(JdbcTemplate jdbcTemplate, SignedUpGateway gateway, PasswordEncoder passwordEncoder) {
		this.jdbcTemplate = jdbcTemplate;
		this.gateway = gateway;
		this.passwordEncoder = passwordEncoder;
	}

	public Account signup(Person person) throws EmailAlreadyOnFileException {
		try {
			jdbcTemplate.update("insert into Member (firstName, lastName, email, password) values (?, ?, ?, ?)",
					person.getFirstName(), person.getLastName(), person.getEmail(), "temporary");
		} catch (DuplicateKeyException e) {
			throw new EmailAlreadyOnFileException(person.getEmail());
		}
		Long accountId = jdbcTemplate.queryForLong("call identity()");
		
		jdbcTemplate.update("update Member set password = ? where id = ?", encrypt(person.getPassword(), accountId), accountId);
		
		Account account = new Account(accountId, person.getFirstName(), person.getLastName(), person.getEmail());
		gateway.signedUp(account);
		return account;
	}

	private String encrypt(String password, Object salt) {
		return passwordEncoder.encodePassword(password, salt);
	}

}