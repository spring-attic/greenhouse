package com.springsource.greenhouse.connect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.encrypt.StringEncryptor;
import org.springframework.stereotype.Repository;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountMapper;
import com.springsource.greenhouse.account.AccountReference;

@Repository
public class JdbcAccountConnectionRepository implements AccountConnectionRepository {

	private final JdbcTemplate jdbcTemplate;

	private final StringEncryptor encryptor;
	
	private final AccountMapper accountMapper;

	public JdbcAccountConnectionRepository(JdbcTemplate jdbcTemplate, StringEncryptor encryptor, AccountMapper accountMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.encryptor = encryptor;
		this.accountMapper = accountMapper;
	}

	public void addConnection(Long accountId, String provider, OAuthToken accessToken, String providerAccountId, String providerProfileUrl) {
		jdbcTemplate.update(INSERT_ACCOUNT_CONNECTION, accountId, provider, encryptor.encrypt(accessToken.getValue()), encryptIfPresent(accessToken.getSecret()), providerAccountId, providerProfileUrl);
	}

	public boolean isConnected(Long accountId, String provider) {
		return jdbcTemplate.queryForInt(SELECT_ACCOUNT_CONNECTION_COUNT, accountId, provider) == 1;
	}

	public void disconnect(Long accountId, String provider) {
		jdbcTemplate.update(DELETE_ACCOUNT_CONNECTION, accountId, provider);
	}

	public OAuthToken getAccessToken(Long accountId, String provider) {
		return jdbcTemplate.queryForObject(SELECT_ACCESS_TOKEN, new RowMapper<OAuthToken>() {
			public OAuthToken mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new OAuthToken(encryptor.decrypt(rs.getString("accessToken")), decryptIfPresent(rs.getString("secret")));
			}
		}, accountId, provider);
	}

	public String getProviderAccountId(Long accountId, String provider) {
		try {
			return jdbcTemplate.queryForObject(SELECT_PROVIDER_ACCOUNT_ID, String.class, accountId, provider);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Account findAccountByConnection(String provider, String accessToken) throws NoSuchAccountConnectionException {
		try {
			return jdbcTemplate.queryForObject(AccountMapper.SELECT_ACCOUNT + " where id = (select member from AccountConnection where provider = ? and accessToken = ?)", accountMapper, provider, encryptor.encrypt(accessToken));
		} catch (EmptyResultDataAccessException e) {
			throw new NoSuchAccountConnectionException(accessToken);
		}
	}

	public List<AccountReference> findAccountsConnectedTo(String provider, List<String> providerAccountIds) {
		NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> params = new HashMap<String, Object>(2, 1);
		params.put("provider", provider);
		params.put("providerAccountIds", providerAccountIds);
		return namedTemplate.query(SELECT_ACCOUNTS_CONNECTED_TO, params, accountMapper.getReferenceMapper());
	}

	// internal helpers

	private String encryptIfPresent(String string) {
		return string != null ? encryptor.encrypt(string) : null;
	}

	private String decryptIfPresent(String string) {
		return string != null ? encryptor.decrypt(string) : null;
	}
	
	private static final String SELECT_PROVIDER_ACCOUNT_ID = "select accountId from AccountConnection where member = ? and provider = ?";

	private static final String SELECT_ACCOUNT_CONNECTION_COUNT = "select exists(select 1 from AccountConnection where member = ? and provider = ?)";

	private static final String INSERT_ACCOUNT_CONNECTION = "insert into AccountConnection (member, provider, accessToken, secret, accountId, profileUrl) values (?, ?, ?, ?, ?, ?)";

	private static final String DELETE_ACCOUNT_CONNECTION = "delete from AccountConnection where member = ? and provider = ?";

	private static final String SELECT_ACCESS_TOKEN = "select accessToken, secret from AccountConnection where member = ? and provider = ?";

	private static final String SELECT_ACCOUNTS_CONNECTED_TO = AccountMapper.SELECT_ACCOUNT_REFERENCE + " where id in (select member from AccountConnection where provider = :provider and accountId in ( :providerAccountIds ))";

}