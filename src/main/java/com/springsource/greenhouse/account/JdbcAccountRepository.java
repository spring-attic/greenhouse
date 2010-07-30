package com.springsource.greenhouse.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

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
	
	public Account findByConnectedAccount(String provider, String accessToken) throws ConnectedAccountNotFoundException {
		try {
			return jdbcTemplate.queryForObject(SELECT_BY_ACCESS_TOKEN, accountMapper, provider, accessToken);
		} catch (EmptyResultDataAccessException e) {
			throw new ConnectedAccountNotFoundException(accessToken, provider);
		}
	}
	
	public List<Account> findFriendAccounts(String provider, List<String> friendIds) {
		NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> params = new HashMap<String, Object>(2, 1);
		params.put("provider", provider);
		params.put("friendIds", friendIds);
		return namedTemplate.query(SELECT_FRIEND_ACCOUNTS, params, accountMapper);
	}

	public void connect(Long id, String provider, String accessToken, String accountId) {
		jdbcTemplate.update(INSERT_CONNECTED_ACCOUNT, id, provider, accessToken, accountId);
	}

	public boolean isConnected(Long id, String provider) {
		return jdbcTemplate.queryForInt(SELECT_CONNECTION_COUNT, id, provider) == 1;		
	}

	public void disconnect(Long id, String provider) {
		jdbcTemplate.update(DELETE_CONNECTED_ACCOUNT, id, provider);
	}

	private static final String SELECT_BY_ID = 
		"select id, firstName, lastName, email, username from Member where id = ?";
	
	private static final String SELECT_BY_EMAIL = 
		"select id, firstName, lastName, email, username from Member where email = ?";

	private static final String SELECT_BY_USERNAME = 
		"select id, firstName, lastName, email, username from Member where username = ?";
	
	private static final String SELECT_BY_ACCESS_TOKEN = "select m.id, m.firstName, m.lastName, m.email, m.username from ConnectedAccount a " + 
		"inner join Member m on a.member = m.id and a.provider = ? and a.accessToken = ?";

	private static final String SELECT_FRIEND_ACCOUNTS = 
		"select m.id, m.firstName, m.lastName, m.email, m.username from ConnectedAccount a " + 
		"inner join Member m on a.member = m.id where provider = :provider and accountId in ( :friendIds )";

	private static final String INSERT_CONNECTED_ACCOUNT = 
		"insert into ConnectedAccount (member, provider, accessToken, accountId) values (?, ?, ?, ?)";
	
	private static final String SELECT_CONNECTION_COUNT = 
		"select count(*) from ConnectedAccount where member = ? and provider = ?";

	private static final String DELETE_CONNECTED_ACCOUNT = 
		"delete from ConnectedAccount where member = ? and provider = ?";
	
}