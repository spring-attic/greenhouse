package com.springsource.greenhouse.connect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scribe.extractors.BaseStringExtractorImpl;
import org.scribe.extractors.HeaderExtractorImpl;
import org.scribe.extractors.TokenExtractorImpl;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth10aServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.services.HMACSha1SignatureService;
import org.scribe.services.TimestampServiceImpl;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.encrypt.StringEncryptor;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountMapper;

abstract class JdbcAccountProvider<A> implements AccountProvider<A> {
	
	private final AccountProviderParameters parameters;

	private final JdbcTemplate jdbcTemplate;

	private final StringEncryptor encryptor;
	
	private final AccountMapper accountMapper;

	public JdbcAccountProvider(AccountProviderParameters parameters, JdbcTemplate jdbcTemplate, StringEncryptor encryptor, AccountMapper accountMapper) {
		this.parameters = parameters;
		this.jdbcTemplate = jdbcTemplate;
		this.encryptor = encryptor;
		this.accountMapper = accountMapper;
	}

	public String getName() {
		return parameters.getName();
	}
	
	public String getApiKey() {
		return parameters.getApiKey();
	}

	public Long getAppId() {
		return parameters.getAppId();
	}
	
	public OAuthToken getRequestToken() {
		Token requestToken = getOAuthService().getRequestToken();
		return new OAuthToken(requestToken.getToken(), requestToken.getSecret());
	}

	public String getAuthorizeUrl(String requestToken) {
		return parameters.getAuthorizeUrl().expand(requestToken).toString();
	}

	public A connect(Long accountId, OAuthToken requestToken, String verifier) {
		OAuthToken accessToken = getAccessToken(requestToken, verifier);
		jdbcTemplate.update(INSERT_ACCOUNT_CONNECTION, accountId, getName(), accessToken.getValue(), accessToken.getSecret(), accountId);
		return createApi(accessToken);
	}

	public A addConnection(Long accountId, String accessToken, String providerAccountId) {
		jdbcTemplate.update(INSERT_ACCOUNT_CONNECTION, accountId, getName(), accessToken, null, accountId);
		return createApi(new OAuthToken(accessToken));
	}

	public boolean isConnected(Long accountId) {
		return jdbcTemplate.queryForInt(SELECT_ACCOUNT_CONNECTION_COUNT, accountId, getName()) == 1;
	}
	
	@Transactional
	public A getApi(Long accountId) {
		if (accountId == null || !isConnected(accountId)) {
			return createApi(null);
		}
		OAuthToken accessToken = jdbcTemplate.queryForObject(SELECT_ACCESS_TOKEN, new RowMapper<OAuthToken>() {
			public OAuthToken mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new OAuthToken(encryptor.decrypt(rs.getString("accessToken")), encryptor.decrypt(rs.getString("secret")));
			}
			
		}, getName(), accountId);
		return createApi(accessToken);
	}

	public void updateProviderAccountId(Long accountId, String providerAccountId) {
		jdbcTemplate.update("update AccountConnection set accountId = ? where provider = ? and member = ?", providerAccountId, getName(), accountId);
	}

	public String getProviderAccountId(Long accountId) {
		try {
			return jdbcTemplate.queryForObject(SELECT_PROVIDER_ACCOUNT_ID, String.class, getName(), accountId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Account findAccountByConnection(String accessToken) throws NoSuchAccountConnectionException {
		try {
			return jdbcTemplate.queryForObject(AccountMapper.SELECT_ACCOUNT + " where id = (select member from AccountConnection where provider = ? and accessToken = ?)", accountMapper, getName(), encryptor.encrypt(accessToken));
		} catch (EmptyResultDataAccessException e) {
			throw new NoSuchAccountConnectionException(accessToken);
		}
	}

	public List<Account> findAccountsWithProviderAccountIds(List<String> providerAccountIds) {
		NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> params = new HashMap<String, Object>(2, 1);
		params.put("provider", getName());
		params.put("providerAccountIds", providerAccountIds);
		return namedTemplate.query(SELECT_ACCOUNTS_WITH_PROVIDER_ACCOUNT_IDS, params, accountMapper);
	}

	public void disconnect(Long accountId) {
		jdbcTemplate.update(DELETE_ACCOUNT_CONNECTION, accountId, getName());
	}

	// subclassing hooks
	
	protected abstract A createApi(OAuthToken accessToken);

	protected String getSecret() {
		return parameters.getSecret();
	}
	
	// internal helpers
	
	private OAuthService getOAuthService() {
		OAuthConfig config = new OAuthConfig();
		config.setRequestTokenEndpoint(parameters.getRequestTokenUrl());
		config.setAccessTokenEndpoint(parameters.getAccessTokenUrl());
		config.setAccessTokenVerb(Verb.POST);
		config.setRequestTokenVerb(Verb.POST);
		config.setApiKey(parameters.getApiKey());
		config.setApiSecret(parameters.getSecret());
		if (parameters.getCallbackUrl() != null) {
			config.setCallback(parameters.getCallbackUrl());
		}
		return new OAuth10aServiceImpl(new HMACSha1SignatureService(), new TimestampServiceImpl(), new BaseStringExtractorImpl(), new HeaderExtractorImpl(), new TokenExtractorImpl(), new TokenExtractorImpl(), config);
	}

	private OAuthToken getAccessToken(OAuthToken requestToken, String verifier) {
		Token accessToken = getOAuthService().getAccessToken(new Token(requestToken.getValue(), requestToken.getSecret()), new Verifier(verifier));
		return new OAuthToken(accessToken.getToken(), accessToken.getSecret());
	}

	private static final String SELECT_PROVIDER_ACCOUNT_ID = "select accountId from AccountConnection where provider = ? and member = ?";

	private static final String SELECT_ACCOUNT_CONNECTION_COUNT = "select count(*) from AccountConnection where member = ? and provider = ?";

	private static final String INSERT_ACCOUNT_CONNECTION = "insert into AccountConnection (member, provider, accessToken, secret, accountId) values (?, ?, ?, ?, ?)";

	private static final String SELECT_ACCESS_TOKEN = "select accessToken, secret from AccountConnection where provider = ? and member = ?";

	private static final String SELECT_ACCOUNTS_WITH_PROVIDER_ACCOUNT_IDS = AccountMapper.SELECT_ACCOUNT + " where id in (select member from AccountConnection where provider = :provider and accountId in ( :providerAccountIds ))";

	private static final String DELETE_ACCOUNT_CONNECTION = "delete from AccountConnection where member = ? and provider = ?";
}