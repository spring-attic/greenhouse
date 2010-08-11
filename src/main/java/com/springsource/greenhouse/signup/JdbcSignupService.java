package com.springsource.greenhouse.signup;

import javax.inject.Inject;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.account.Account;

@Service
@Transactional
public class JdbcSignupService implements SignupService {

	private JdbcTemplate jdbcTemplate;
	
	private SignedUpGateway gateway;
	
	@Inject
	public JdbcSignupService(JdbcTemplate jdbcTemplate, SignedUpGateway gateway) {
		this.jdbcTemplate = jdbcTemplate;
		this.gateway = gateway;
	}

	public Account signup(Person person) throws EmailAlreadyOnFileException {
		try {
			jdbcTemplate.update("insert into Member (firstName, lastName, email, password) values (?, ?, ?, ?)",
					person.getFirstName(), person.getLastName(), person.getEmail(), encrypt(person.getPassword()));
		} catch (DuplicateKeyException e) {
			throw new EmailAlreadyOnFileException(person.getEmail());
		}
		Long accountId = jdbcTemplate.queryForLong("call identity()");
		Account account = new Account(accountId, person.getFirstName(), person.getLastName(), person.getEmail());
		gateway.signedUp(account);
		return account;
	}

	private String encrypt(String password) {
		// TODO encrypt password		
		return password;
	}

}