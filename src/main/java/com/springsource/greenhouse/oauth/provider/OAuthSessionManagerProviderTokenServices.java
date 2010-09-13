package com.springsource.greenhouse.oauth.provider;

import java.util.Collection;

import javax.inject.Inject;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth.provider.token.InvalidOAuthTokenException;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;

import com.springsource.greenhouse.account.Account;
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
	
	public OAuthProviderToken createUnauthorizedRequestToken(String consumerKey, String callbackUrl) {
		return providerTokenFor(sessionManager.newOAuthSession(consumerKey, callbackUrl));
	}

	public void authorizeRequestToken(String requestToken, String verifier, Authentication authentication) {
		if (!(authentication.getPrincipal() instanceof Account)) {
			throw new IllegalArgumentException("Authenticated user principal is not of expected Account type");
		}
		Long authorizingAccountId = ((Account) authentication.getPrincipal()).getId();
		sessionManager.authorize(requestToken, authorizingAccountId, verifier);
	}
	
	public OAuthAccessProviderToken createAccessToken(String requestToken) {
		return providerTokenFor(sessionManager.grantAccess(requestToken));
	}

	public OAuthProviderToken getToken(String tokenValue) {
		OAuthSession session = sessionManager.getSession(tokenValue);
		if (session != null) {
			return providerTokenFor(session);
		}
		try {
			ConnectedApp connectedApp = accountRepository.findConnectedApp(tokenValue);
			return providerTokenFor(connectedApp);			
		} catch (ConnectedAppNotFoundException e) {
			throw new InvalidOAuthTokenException("Could not find OAuthSession or AppConnection for provided OAuth token " + tokenValue);
		}
	}
	
	// internal helpers
	
	private OAuthProviderToken providerTokenFor(OAuthSession session) {
		return new OAuthSessionProviderToken(session);
	}
	
	private OAuthAccessProviderToken providerTokenFor(ConnectedApp connectedApp) {
		return new ConnectedAppProviderToken(connectedApp, accountRepository);
	}	

	@SuppressWarnings("serial")
	private static class OAuthSessionProviderToken implements OAuthProviderToken {

		private OAuthSession session;

		public OAuthSessionProviderToken(OAuthSession session) {
			this.session = session;
		}

		public String getConsumerKey() {
			return session.getApiKey();
		}
		
		public String getValue() {
			return session.getRequestToken();
		}
		
		public String getSecret() {
			return session.getSecret();
		}
		
		public String getCallbackUrl() {
			return session.getCallbackUrl();
		}

		public String getVerifier() {
			return session.getVerifier();
		}

		public boolean isAccessToken() {
			return false;
		}			
	}
	
	@SuppressWarnings("serial")
	private static class ConnectedAppProviderToken implements OAuthAccessProviderToken {

		private ConnectedApp connection;

		private AccountRepository accountRepository;
		
		private Authentication userAuthentication;
		
		public ConnectedAppProviderToken(ConnectedApp connection, AccountRepository accountRepository) {
			this.connection = connection;
		}

		public String getConsumerKey() {
			return connection.getApiKey();
		}
		
		public String getValue() {
			return connection.getAccessToken();
		}
		
		public String getSecret() {
			return connection.getSecret();
		}
		
		public String getCallbackUrl() {
			return null;
		}

		public String getVerifier() {
			return null;
		}

		public boolean isAccessToken() {
			return true;
		}
		
		public Authentication getUserAuthentication() {
			if (userAuthentication == null) {
				Account account = accountRepository.findById(connection.getAccountId());
				userAuthentication = new UsernamePasswordAuthenticationToken(account, null, (Collection<GrantedAuthority>)null);		
			}
			return userAuthentication;
		}
			
	}

}