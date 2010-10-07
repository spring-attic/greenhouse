package com.springsource.greenhouse.connect;

import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.account.Account;

abstract class AbstractAccountProvider<A> implements AccountProvider<A> {
	
	private final AccountProviderParameters parameters;

	private final AccountConnectionRepository connectionRepository;
	
	public AbstractAccountProvider(AccountProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		this.parameters = parameters;
		this.connectionRepository = connectionRepository;
	}

	// provider meta-data
	
	public String getName() {
		return parameters.getName();
	}
	
	public String getDisplayName() {
		return parameters.getDisplayName();
	}

	public String getApiKey() {
		return parameters.getApiKey();
	}

	public Long getAppId() {
		return parameters.getAppId();
	}
	
	// connection management
	
	public OAuthToken fetchNewRequestToken(String callbackUrl) {
		Token requestToken = getOAuthService(callbackUrl).getRequestToken();
		return new OAuthToken(requestToken.getToken(), requestToken.getSecret());
	}

	public String buildAuthorizeUrl(String requestToken) {
		return parameters.getAuthorizeUrl().expand(requestToken).toString();
	}

	public void connect(Long accountId, OAuthToken requestToken, String verifier) {
		OAuthToken accessToken = getAccessToken(requestToken, verifier);
		A api = createApi(accessToken);
		connectionRepository.addConnection(accountId, getName(), accessToken, getProviderAccountId(api), getProviderProfileUrl(api));
	}

	public void addConnection(Long accountId, String accessToken, String providerAccountId) {
		A api = createApi(new OAuthToken(accessToken));
		connectionRepository.addConnection(accountId, getName(), accessToken, providerAccountId, getProviderProfileUrl(api));
	}

	public boolean isConnected(Long accountId) {
		return connectionRepository.isConnected(accountId, getName());
	}

	public void disconnect(Long accountId) {
		connectionRepository.disconnect(accountId, getName());
	}
	
	@Transactional
	public A getApi(Long accountId) {
		if (accountId == null || !isConnected(accountId)) {
			return createApi(null);
		}
		OAuthToken accessToken = connectionRepository.getAccessToken(accountId, getName());
		return createApi(accessToken);
	}

	// additional finders
	
	public String getProviderAccountId(Long accountId) {
		return connectionRepository.getProviderAccountId(accountId, getName());
	}

	public Account findAccountByConnection(String accessToken) throws NoSuchAccountConnectionException {
		return connectionRepository.findAccountByConnection(getName(), accessToken);
	}

	public List<Account> findAccountsWithProviderAccountIds(List<String> providerAccountIds) {
		return connectionRepository.findAccountsWithProviderAccountIds(getName(), providerAccountIds);
	}

	// subclassing hooks
	
	protected abstract A createApi(OAuthToken accessToken);

	protected abstract String getProviderAccountId(A api);
	
	protected String getSecret() {
		return parameters.getSecret();
	}
	
	// internal helpers
	
	private OAuthService getOAuthService() {
		return getOAuthService(null);
	}
	
	private OAuthService getOAuthService(String callbackUrl) {
		OAuthConfig config = new OAuthConfig();
		config.setRequestTokenEndpoint(parameters.getRequestTokenUrl());
		config.setAccessTokenEndpoint(parameters.getAccessTokenUrl());
		config.setAccessTokenVerb(Verb.POST);
		config.setRequestTokenVerb(Verb.POST);
		config.setApiKey(parameters.getApiKey());
		config.setApiSecret(parameters.getSecret());
		if (callbackUrl != null) {
			config.setCallback(callbackUrl);
		}
		return new OAuth10aServiceImpl(new HMACSha1SignatureService(), new TimestampServiceImpl(), new BaseStringExtractorImpl(), new HeaderExtractorImpl(), new TokenExtractorImpl(), new TokenExtractorImpl(), config);
	}

	private OAuthToken getAccessToken(OAuthToken requestToken, String verifier) {
		Token accessToken = getOAuthService().getAccessToken(new Token(requestToken.getValue(), requestToken.getSecret()), new Verifier(verifier));
		return new OAuthToken(accessToken.getToken(), accessToken.getSecret());
	}

}