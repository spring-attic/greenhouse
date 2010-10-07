package com.springsource.greenhouse.connect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.encrypt.StringEncryptor;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.linkedin.LinkedInOperations;
import org.springframework.social.tripit.TripItOperations;
import org.springframework.social.twitter.TwitterOperations;

import com.springsource.greenhouse.account.AccountMapper;

//TODO this is disabled because it's needed in root-context yet scanned by app-servlet-context. not very clean: revisit this.
//@Repository
public class JdbcAccountProviderFactory implements AccountProviderFactory {
	
	private final JdbcTemplate jdbcTemplate;

	private final AccountMapper accountMapper;

	private final StringEncryptor encryptor;
	
	@Autowired
	public JdbcAccountProviderFactory(JdbcTemplate jdbcTemplate, StringEncryptor encryptor, AccountMapper accountMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.accountMapper = accountMapper;
		this.encryptor = encryptor;
	}

	@SuppressWarnings("unchecked")
	public <A> AccountProvider<A> getAccountProvider(Class<A> apiType) {
		if (FacebookOperations.class.equals(apiType)) {
			return (AccountProvider<A>) new FacebookAccountProvider(getParameters("facebook"), new JdbcAccountConnectionRepository(jdbcTemplate, encryptor, accountMapper));
		} else if (TwitterOperations.class.equals(apiType)) {
			return (AccountProvider<A>) new TwitterAccountProvider(getParameters("twitter"), new JdbcAccountConnectionRepository(jdbcTemplate, encryptor, accountMapper));			
		} else if (LinkedInOperations.class.equals(apiType)) {
			return (AccountProvider<A>) new LinkedInAccountProvider(getParameters("linkedin"), new JdbcAccountConnectionRepository(jdbcTemplate, encryptor, accountMapper));
		} else if (TripItOperations.class.equals(apiType)) {
			return (AccountProvider<A>) new TripItAccountProvider(getParameters("tripit"), new JdbcAccountConnectionRepository(jdbcTemplate, encryptor, accountMapper));
		} else {
			throw new IllegalArgumentException("Not a supported apiType " + apiType);
		}
	}

	public AccountProvider<?> getAccountProviderByName(String name) {
		if (name.equals("facebook")) {
			return getAccountProvider(FacebookOperations.class);
		} else if (name.equals("twitter")) {
			return getAccountProvider(TwitterOperations.class);
		} else if (name.equals("linkedin")) {
			return getAccountProvider(LinkedInOperations.class);
		} else if (name.equals("tripit")) {
			return getAccountProvider(TripItOperations.class);
		} else {
			throw new IllegalArgumentException("Not a known provider: " + name);
		}
	}
	
	public List<ConnectedProfile> findConnectedProfiles(Long accountId) {
		return jdbcTemplate.query(SELECT_ACCOUNT_CONNECTIONS, new RowMapper<ConnectedProfile>() {
			public ConnectedProfile mapRow(ResultSet rs, int row) throws SQLException {
				return new ConnectedProfile(rs.getString("displayName"), rs.getString("profileUrl"));
			}
		}, accountId);
	}
	
	// internal helpers
	
	private AccountProviderParameters getParameters(final String provider) {
		return jdbcTemplate.queryForObject(SELECT_ACCOUNT_PROVIDER_BY_NAME, new RowMapper<AccountProviderParameters>() {
			public AccountProviderParameters mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new AccountProviderParameters(provider, rs.getString("displayName"),
					encryptor.decrypt(rs.getString("apiKey")), encryptor.decrypt(rs.getString("secret")), rs.getLong("appId"),
					rs.getString("requestTokenUrl"), rs.getString("authorizeUrl"), rs.getString("accessTokenUrl"));
			}
		}, provider);
	}

	private static final String SELECT_ACCOUNT_PROVIDER_BY_NAME = "select displayName, apiKey, secret, appId, requestTokenUrl, authorizeUrl, accessTokenUrl from AccountProvider where name = ?";

	private static final String SELECT_ACCOUNT_CONNECTIONS = "select p.name, p.displayName, c.accountId, c.profileUrl from AccountConnection c inner join AccountProvider p on c.provider = p.name where member = ? order by displayName";

}