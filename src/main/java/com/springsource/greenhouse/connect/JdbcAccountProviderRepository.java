package com.springsource.greenhouse.connect;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


public class JdbcAccountProviderRepository implements AccountProviderRepository {
	private static final String SELECT_ACCOUNT_PROVIDER_BY_NAME = 
 "select name, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl from AccountProvider where name = ?";
	private final JdbcTemplate jdbcTemplate;

	public JdbcAccountProviderRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public AccountProvider findAccountProviderByName(String name) {
		return jdbcTemplate.queryForObject(SELECT_ACCOUNT_PROVIDER_BY_NAME, new RowMapper<AccountProvider>() {
			public AccountProvider mapRow(ResultSet rs, int row) throws SQLException {
				AccountProvider provider = new JdbcAccountProvider(jdbcTemplate);
				provider.setName(rs.getString("name"));
				provider.setApiKey(rs.getString("apiKey"));
				provider.setApiSecret(rs.getString("secret"));
				provider.setRequestTokenUrl(rs.getString("requestTokenUrl"));
				provider.setAuthorizeUrl(rs.getString("authorizeUrl"));
				provider.setAccessTokenUrl(rs.getString("accessTokenUrl"));
				return provider;
			}
		}, name);
	}
}
