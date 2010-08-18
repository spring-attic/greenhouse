package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.springsource.greenhouse.utils.EmailUtils;

public class JdbcAccountRepository implements AccountRepository {
	
	private final JdbcTemplate jdbcTemplate;
	
	private final AccountMapper accountMapper = new AccountMapper();
	
	private final PasswordEncoder passwordEncoder = new ShaHashedRandomSaltedPasswordEncoder();
	
	@Inject
	public JdbcAccountRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Account createAccount(Person person) throws EmailAlreadyOnFileException {
		try {
			jdbcTemplate.update("insert into Member (firstName, lastName, email, password) values (?, ?, ?, ?)",
					person.getFirstName(), person.getLastName(), person.getEmail(), passwordEncoder.encode(person.getPassword()));
		} catch (DuplicateKeyException e) {
			throw new EmailAlreadyOnFileException(person.getEmail());
		}
		Long accountId = jdbcTemplate.queryForLong("call identity()");
		return new Account(accountId, person.getFirstName(), person.getLastName(), person.getEmail());		
	}

	public Account authenticate(String username, String password) throws UsernameNotFoundException, InvalidPasswordException {
		String query = EmailUtils.isEmail(username) ? 
				"select id, firstName, lastName, email, username, password from Member where email = ?" : 
				"select id, firstName, lastName, email, username, password from Member where username = ?";
		try {
			return jdbcTemplate.queryForObject(query, passwordProtectedAccountMapper, username).accessAccount(password, passwordEncoder);
		} catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException(username);
		}
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
			// TODO handle case where accessToken is invalid
			return jdbcTemplate.queryForObject(SELECT_BY_ACCESS_TOKEN, accountMapper, provider, accessToken);
		} catch (EmptyResultDataAccessException e) {
			throw new ConnectedAccountNotFoundException(provider);
		}
	}
	
	public List<Account> findFriendAccounts(String provider, List<String> friendIds) {
		NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> params = new HashMap<String, Object>(2, 1);
		params.put("provider", provider);
		params.put("friendIds", friendIds);
		return namedTemplate.query(SELECT_FRIEND_ACCOUNTS, params, accountMapper);
	}

	public void connect(Long id, String provider, String accessToken, String accountId) throws AccountAlreadyConnectedException {
		try {
			jdbcTemplate.update(INSERT_CONNECTED_ACCOUNT, id, provider, accessToken, accountId);
		} catch (DuplicateKeyException e) {
			throw new AccountAlreadyConnectedException(provider, accountId);
		}
	}

	public boolean isConnected(Long id, String provider) {
		return jdbcTemplate.queryForInt(SELECT_CONNECTION_COUNT, id, provider) == 1;		
	}

	public void disconnect(Long id, String provider) {
		jdbcTemplate.update(DELETE_CONNECTED_ACCOUNT, id, provider);
	}

	private RowMapper<PasswordProtectedAccount> passwordProtectedAccountMapper = new RowMapper<PasswordProtectedAccount>() {
		
		private RowMapper<Account> accountMapper = new AccountMapper();	

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