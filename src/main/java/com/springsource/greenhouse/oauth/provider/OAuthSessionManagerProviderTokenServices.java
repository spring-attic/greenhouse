package com.springsource.greenhouse.oauth.provider;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth.provider.token.InvalidOAuthTokenException;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenImpl;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;

import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.ConnectedApp;
import com.springsource.greenhouse.account.ConnectedAppNotFoundException;

public class OAuthSessionManagerProviderTokenServices implements OAuthProviderTokenServices {
	
	private OAuthSessionManager sessionManager;
	
	private AccountRepository accountRepository;
	
	@Inject
	public OAuthSessionManagerProviderTokenServices(OAuthSessionManager sessionManager, AccountRepository accountRepository) {
		this.sessionManager = sessionManager;
		this.accountRepository = accountRepository;
	}
	
	public OAuthProviderToken createUnauthorizedRequestToken(String consumerKey, String callbackUrl) throws AuthenticationException {
		return providerTokenFor(sessionManager.newOAuthSession(consumerKey, callbackUrl));
	}

	public void authorizeRequestToken(String requestToken, String verifier, Authentication authentication) throws AuthenticationException {
		sessionManager.authorizeRequestToken(requestToken, authentication, verifier);
	}
	
	public OAuthAccessProviderToken createAccessToken(String requestToken) throws AuthenticationException {
		return providerTokenFor(sessionManager.grantAccess(requestToken));
	}

	public OAuthProviderToken getToken(String tokenValue) throws AuthenticationException {
		OAuthSession session = sessionManager.getSession(tokenValue);
		if (session != null) {
			return providerTokenFor(session);
		}
		try {
			ConnectedApp connectedApp = accountRepository.findConnectedApp(tokenValue);
			return providerTokenFor(connectedApp);			
		} catch (ConnectedAppNotFoundException e) {
			throw new InvalidOAuthTokenException("Could not find app connection for provided access token " + tokenValue);
		}
	}
	
	// internal helpers
	
	private OAuthProviderToken providerTokenFor(OAuthSession session) {
		OAuthProviderTokenImpl token = new OAuthProviderTokenImpl();
	    token.setConsumerKey(session.getApiKey());
	    token.setValue(session.getRequestToken());
	    token.setSecret(session.getSecret());	    
	    token.setCallbackUrl(session.getCallbackUrl());
		return token;
	}
	
	private OAuthAccessProviderToken providerTokenFor(ConnectedApp connectedApp) {
		OAuthProviderTokenImpl token = new OAuthProviderTokenImpl();
		token.setAccessToken(true);
	    token.setConsumerKey(connectedApp.getApiKey());
	    token.setValue(connectedApp.getAccessToken());
	    token.setSecret(connectedApp.getSecret());
	    return token;
	}	
	
}