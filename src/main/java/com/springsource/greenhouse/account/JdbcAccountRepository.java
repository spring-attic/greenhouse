package com.springsource.greenhouse.account;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.springsource.greenhouse.utils.EmailUtils;

public class JdbcAccountRepository implements AccountRepository {
	
	private JdbcTemplate jdbcTemplate;
	
	private AccountMapper accountMapper = new AccountMapper();
	
	@Inject
	public JdbcAccountRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Account findById(Long id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, accountMapper, id);
	}
	
	public Account findByUsername(String username) throws UsernameNotFoundException {
		String query = EmailUtils.isEmail(username) ? SELECT_BY_EMAIL : SELECT_BY_USERNAME;
		try {
			return jdbcTemplate.queryForObject(query, accountMapper, username);
		} catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException(username);
		}
	}
	
	public Account findByConnectedAccount(String accountName, String accessToken) throws ConnectedAccountNotFoundException {
		try {
			return jdbcTemplate.queryForObject(SELECT_BY_ACCESS_TOKEN, accountMapper, accountName, accessToken);
		} catch (EmptyResultDataAccessException e) {
			throw new ConnectedAccountNotFoundException(accessToken, accountName);
		}
	}
	
	public void connect(Long id, String accountName, String accessToken) {
		jdbcTemplate.update(INSERT_CONNECTED_ACCOUNT, id, accountName, accessToken);
	}

	public boolean isConnected(Long id, String accountName) {
		return jdbcTemplate.queryForInt(CONNECTION_COUNT, id, accountName) == 1;		
	}

	public void disconnect(Long id, String accountName) {
		jdbcTemplate.update(DELETE_CONNECTED_ACCOUNT, id, accountName);
	}

	private static final String SELECT_BY_ID = 
		"select id, firstName, lastName, email, username from Member where id = ?";
	
	private static final String SELECT_BY_EMAIL = 
		"select id, firstName, lastName, email, username from Member where email = ?";

	private static final String SELECT_BY_USERNAME = 
		"select id, firstName, lastName, email, username from Member where username = ?";
	
	private static final String SELECT_BY_ACCESS_TOKEN = "select m.id, m.firstName, m.lastName, m.email, m.username from ConnectedAccount a " + 
		"inner join Member m on a.member = m.id and a.accountName = ? and a.accessToken = ?";
	
	private static final String DELETE_CONNECTED_ACCOUNT = 
		"delete from ConnectedAccount where member = ? and accountName = ?";
	
	private static final String INSERT_CONNECTED_ACCOUNT = 
		"insert into ConnectedAccount (member, accountName, accessToken) values (?, ?, ?)";
	
	private static final String CONNECTION_COUNT = 
		"select count(*) from ConnectedAccount where member = ? and accountName = ?";
}