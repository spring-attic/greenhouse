package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.springsource.greenhouse.utils.EmailUtils;

public class JdbcAccountRepository implements AccountRepository {	
	private JdbcTemplate jdbcTemplate;
	private AccountMapper accountMapper = new AccountMapper();
	
	@Inject
	public JdbcAccountRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Account findAccount(Long id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, accountMapper, id);
	}
	
	public Account findAccount(String username) throws UsernameNotFoundException {
		String query = EmailUtils.isEmail(username) ? SELECT_BY_EMAIL : SELECT_BY_USERNAME;
		try {
			return jdbcTemplate.queryForObject(query, accountMapper, username);
		} catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException(username);
		}
	}
	
	public Account findByConnectedAccount(String accessToken, String accountName) 
			throws ConnectedAccountNotFoundException {
		try {
			return jdbcTemplate.queryForObject(SELECT_BY_ACCESS_TOKEN, new RowMapper<Account>() {
	        	public Account mapRow(ResultSet rs, int row) throws SQLException {				
	                return new Account(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"),
	                		rs.getString("email"), rs.getString("username"));
	            }
	        }, accessToken, accountName);
		} catch (IncorrectResultSizeDataAccessException e) {
			throw new ConnectedAccountNotFoundException(accessToken, accountName);
		}
	}
	
	public void removeConnectedAccount(Long memberId, String accountName) {
		jdbcTemplate.update(DELETE_CONNECTED_ACCOUNT, memberId, accountName);
	}
	
	public void connectAccount(Long memberId, String externalId, String accountName, String accessToken, String secret) {
		jdbcTemplate.update(CONNECT_ACCOUNT, accessToken, memberId, externalId, accountName, secret);
	}
	
	public boolean isConnected(Long memberId, String accountName) {
		return jdbcTemplate.queryForInt(COUNT_CONNECTIONS_FOR_MEMBER, memberId, accountName) == 1;		
	}
	
	private static final String SELECT_BY_USERNAME = 
		"select id, firstName, lastName, email, username from Member where username = ?";
	private static final String SELECT_BY_EMAIL = 
		"select id, firstName, lastName, email, username from Member where email = ?";
	private static final String SELECT_BY_ID = 
		"select id, firstName, lastName, email, username from Member where id = ?";
	private static final String SELECT_BY_ACCESS_TOKEN = "select id, firstName, lastName, email, username " +
		"from Member, ConnectedAccount where member=id and accessToken=? and accountName=?";
	private static final String DELETE_CONNECTED_ACCOUNT = 
		"delete from ConnectedAccount where member=? and accountName=?";
	private static final String CONNECT_ACCOUNT = 
		"insert into ConnectedAccount (accessToken, member, externalId, accountName, secret) " +
		"values (?, ?, ?, ?, ?)";
	private static final String COUNT_CONNECTIONS_FOR_MEMBER = 
		"select count(*) from ConnectedAccount where member = ? and accountName = ?";
}
