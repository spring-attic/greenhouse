package com.springsource.greenhouse.connect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.BaseStringExtractorImpl;
import org.scribe.extractors.HeaderExtractorImpl;
import org.scribe.extractors.TokenExtractorImpl;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth10aServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.services.HMACSha1SignatureService;
import org.scribe.services.TimestampServiceImpl;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.social.oauth.OAuthClientRequestSigner;
import org.springframework.social.oauth.OAuthSigningClientHttpRequestFactory;
import org.springframework.social.oauth1.ScribeOAuth1RequestSigner;
import org.springframework.social.twitter.TwitterErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountMapper;

class JdbcAccountProvider implements AccountProvider {
	private final String name;
	
	private final String apiKey;
	
	private final String secret;
	
	private final String authorizeUrl;
	
	private final JdbcTemplate jdbcTemplate;

	private final AccountMapper accountMapper;

	private final String requestTokenUrl;

	private final String accessTokenUrl;

	public JdbcAccountProvider(String name, String apiKey, String secret, String requestTokenUrl, String authorizeUrl,
			String accessTokenUrl, JdbcTemplate jdbcTemplate, AccountMapper accountMapper) {
		this.name = name;
		this.apiKey = apiKey;
		this.secret = secret;
		this.requestTokenUrl = requestTokenUrl;
		this.authorizeUrl = authorizeUrl;
		this.accessTokenUrl = accessTokenUrl;
		this.jdbcTemplate = jdbcTemplate;
		this.accountMapper = accountMapper;
	}

	public String getName() {
		return name;
	}
	
	public String getApiKey() {
		return apiKey;
	}

	public Token fetchNewRequestToken(String callbackUrl) {
		org.scribe.model.Token requestToken = getOAuthService(callbackUrl).getRequestToken();
		return new Token(requestToken.getToken(), requestToken.getSecret());
	}

	public String getAuthorizeUrl() {
		return authorizeUrl;
	}

	public Token fetchAccessToken(Token requestToken, String verifier) {
		try {
			org.scribe.model.Token scribeRequestToken = new org.scribe.model.Token(requestToken.getValue(),
					requestToken.getSecret());
			org.scribe.model.Token accessToken = getOAuthService(null).getAccessToken(scribeRequestToken,
					new Verifier(verifier));
			return new Token(accessToken.getToken(), accessToken.getSecret());
		} catch (OAuthException e) {
			return null;
		}
	}

	public void connect(Long accountId, ConnectionDetails details) {
		try {
			jdbcTemplate.update(INSERT_ACCOUNT_CONNECTION, accountId, name, details.getAccessToken(),
					details.getAccessTokenSecret(), accountId);
		} catch (DuplicateKeyException e) {
			// TODO: What to do here? Previously it was:
			// throw new AccountConnectionAlreadyExists(name, accountId);
		}
	}

	public boolean isConnected(Long accountId) {
		return jdbcTemplate.queryForInt(SELECT_ACCOUNT_CONNECTION_COUNT, accountId, name) == 1;
	}

	public RestOperations getApi(Long accountId) {
		if (isConnected(accountId)) {
			return jdbcTemplate.queryForObject(SELECT_ACCOUNT_CONNECTION, apiMapper, name, accountId);
		} else {
			// TODO address duplication with RowMapper below
			RestTemplate rest = new RestTemplate();
			rest.setErrorHandler(new TwitterErrorHandler());
			// Facebook uses "text/javascript" as the JSON content type
			MappingJacksonHttpMessageConverter json = new MappingJacksonHttpMessageConverter();
			json.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "javascript")));
			rest.getMessageConverters().add(json);
			return rest;			
		}
	}

	public void saveProviderAccountId(Long accountId, String providerAccountId) {
		jdbcTemplate.update("update AccountConnection set accountId = ? where provider = ? and member = ?",
				providerAccountId, name, accountId);
	}

	public String getProviderAccountId(Long accountId) {
		try {
			return jdbcTemplate.queryForObject(SELECT_PROVIDER_ACCOUNT_ID, String.class, name, accountId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Set<Account> findAccountsWithProviderAccountIds(Collection<String> providerAccountIds) {
		NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> params = new HashMap<String, Object>(2, 1);
		params.put("provider", name);
		params.put("friendIds", providerAccountIds);
		return new HashSet<Account>(namedTemplate.query(SELECT_ACCOUNTS_WITH_PROVIDER_ACCOUNT_IDS, params,
				accountMapper));
	}

	public void disconnect(Long accountId) {
		jdbcTemplate.update(DELETE_ACCOUNT_CONNECTION, accountId, name);
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

	private OAuthService getOAuthService(String callbackUrl) {
		OAuthConfig config = new OAuthConfig();
		config.setRequestTokenEndpoint(requestTokenUrl);
		config.setAccessTokenEndpoint(accessTokenUrl);
		config.setAccessTokenVerb(Verb.POST);
		config.setRequestTokenVerb(Verb.POST);
		config.setApiKey(apiKey);
		config.setApiSecret(secret);
		if (callbackUrl != null) {
			config.setCallback(callbackUrl);
		}

		return new OAuth10aServiceImpl(new HMACSha1SignatureService(), new TimestampServiceImpl(),
				new BaseStringExtractorImpl(), new HeaderExtractorImpl(), new TokenExtractorImpl(),
				new TokenExtractorImpl(), config);
	}

	protected OAuthClientRequestSigner getRequestSigner(String accessToken, String accessTokenSecret) {
		return new ScribeOAuth1RequestSigner(apiKey, secret, accessToken, accessTokenSecret);
	}
	
	private static final String SELECT_ACCOUNT_CONNECTION = "select accessToken, secret, provider from AccountConnection where provider = ? and member = ?";

	private static final String SELECT_PROVIDER_ACCOUNT_ID = "select accountId from AccountConnection where provider = ? and member = ?";

	private static final String SELECT_ACCOUNT_CONNECTION_COUNT = "select count(*) from AccountConnection where member = ? and provider = ?";

	private static final String INSERT_ACCOUNT_CONNECTION = "insert into AccountConnection (member, provider, accessToken, secret, accountId) values (?, ?, ?, ?, ?)";

	private static final String SELECT_ACCOUNTS_WITH_PROVIDER_ACCOUNT_IDS = "select id, firstName, lastName, email, username, gender, pictureSet "
			+ "from Member where id in (select member from AccountConnection where provider = :provider and accountId in ( :friendIds )) ";

	private static final String DELETE_ACCOUNT_CONNECTION = "delete from AccountConnection where member = ? and provider = ?";
}