package com.springsource.greenhouse.signup;

import javax.inject.Inject;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.account.Account;

@Service
@Transactional
public class DefaultSignupService implements SignupService {

	private JdbcTemplate jdbcTemplate;
	
	private SignedUpMessageGateway messageGateway;
	
	@Inject
	public DefaultSignupService(JdbcTemplate jdbcTemplate, SignedUpMessageGateway messageGateway) {
		this.jdbcTemplate = jdbcTemplate;
		this.messageGateway = messageGateway;
	}

	public Account signup(Person person) throws EmailAlreadyOnFileException {
		try {
			jdbcTemplate.update("insert into Member (firstName, lastName, email, password) values (?, ?, ?, ?)",
					person.getFirstName(), person.getLastName(), person.getEmail(), encrypt(person.getPassword()));
		} catch (DuplicateKeyException e) {
			throw new EmailAlreadyOnFileException(person.getEmail());
		}
		Long memberId = jdbcTemplate.queryForLong("call identity()");
		Account account = new Account(memberId, person.getFirstName(), person.getLastName(), person.getEmail());
		messageGateway.signedUp(account);
		return account;
	}

	private String encrypt(String password) {
		// TODO encrypt password		
		return password;
	}

}