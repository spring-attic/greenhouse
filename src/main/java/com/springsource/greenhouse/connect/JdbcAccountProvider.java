package com.springsource.greenhouse.connect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.social.oauth.OAuthClientRequestSigner;
import org.springframework.social.oauth.OAuthSigningClientHttpRequestFactory;
import org.springframework.social.oauth1.ScribeOAuth1RequestSigner;
import org.springframework.social.twitter.TwitterErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.springsource.greenhouse.account.Account;

class JdbcAccountProvider implements AccountProvider {
	private final String name;
	
	private final String apiKey;
	
	private final String secret;
	
	private final String authorizeUrl;
	
	private final JdbcTemplate jdbcTemplate;

	public JdbcAccountProvider(String name, String apiKey, String secret, String requestTokenUrl,
			String authorizeUrl, String accessTokenUrl, JdbcTemplate jdbcTemplate) {
		this.name = name;
		this.apiKey = apiKey;
		this.secret = secret;
		this.authorizeUrl = authorizeUrl;
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getName() {
		return name;
	}
	
	public String getApiKey() {
		return apiKey;
	}

	public Token fetchNewRequestToken() {
		// TODO
		return null;
	}

	public String getAuthorizeUrl() {
		return authorizeUrl;
	}

	public Token fetchAccessToken(Token requestToken, String verifier) {
		// TODO
		return null;
	}

	public void connect(Long accountId, ConnectionDetails details) {
		// TODO
	}

	public boolean isConnected(Long accountId) {
		return jdbcTemplate.queryForInt(SELECT_ACCOUNT_CONNECTION_COUNT, accountId, name) == 1;
	}

	public RestOperations getApi(Long accountId) {
		return jdbcTemplate.queryForObject(SELECT_ACCOUNT_CONNECTION, apiMapper, name, accountId);
	}

	public void saveProviderAccountId(Long accountId, String providerAccountId) {
		// TODO
	}

	public String getProviderAccountId(Long accountId) {
		try {
			return jdbcTemplate.queryForObject(SELECT_PROVIDER_ACCOUNT_ID, String.class, name, accountId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Set<Account> findAccountsWithProviderAccountIds(Collection<String> providerAccountIds) {
		// TODO
		return null;
	}

	public void disconnect(Long accountId) {
		// TODO
	}

	// internal helpers
	
	private final RowMapper<RestOperations> apiMapper = new RowMapper<RestOperations>() {
		public RestOperations mapRow(ResultSet rs, int row) throws SQLException {
			RestTemplate rest = new RestTemplate(new OAuthSigningClientHttpRequestFactory(
					getRequestSigner(rs.getString("accessToken"), rs.getString("secret"))));
			rest.setErrorHandler(new TwitterErrorHandler());
			// Facebook uses "text/javascript" as the JSON content type
			MappingJacksonHttpMessageConverter json = new MappingJacksonHttpMessageConverter();
			json.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "javascript")));
			rest.getMessageConverters().add(json);
			return rest;
		}
	};

	protected OAuthClientRequestSigner getRequestSigner(String accessToken, String accessTokenSecret) {
		return new ScribeOAuth1RequestSigner(apiKey, secret, accessToken, accessTokenSecret);
	}
	
	private static final String SELECT_ACCOUNT_CONNECTION = "select accessToken, secret, provider from AccountConnection where provider = ? and member = ?";

	private static final String SELECT_PROVIDER_ACCOUNT_ID = "select accountId from AccountConnection where provider = ? and member = ?";

	private static final String SELECT_ACCOUNT_CONNECTION_COUNT = "select count(*) from AccountConnection where member = ? and provider = ?";

}