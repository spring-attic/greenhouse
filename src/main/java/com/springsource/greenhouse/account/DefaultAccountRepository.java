package com.springsource.greenhouse.account;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.springsource.greenhouse.utils.EmailUtils;

public class DefaultAccountRepository implements AccountRepository {

	private JdbcTemplate jdbcTemplate;
	
	private AccountMapper accountMapper = new AccountMapper();
	
	@Inject
	public DefaultAccountRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Account findAccount(Long id) {
		return jdbcTemplate.queryForObject("select id, firstName, lastName, email, username from Member where id = ?", accountMapper, id);
	}
	
	public Account findAccount(String username) throws AccountNotFoundException {
		String query = EmailUtils.isEmail(username) ? 
			"select id, firstName, lastName, email, username from Member where email = ?" : 
			"select id, firstName, lastName, email, username from Member where username = ?";
		try {
			return jdbcTemplate.queryForObject(query, accountMapper, username);
		} catch (EmptyResultDataAccessException e) {
			throw new AccountNotFoundException(username);
		}
	}
	
}
