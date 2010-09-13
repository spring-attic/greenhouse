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
import com.springsource.greenhouse.account.AppConnection;
import com.springsource.greenhouse.account.InvalidAccessTokenException;

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
		try {
			Long authorizingAccountId = ((Account) authentication.getPrincipal()).getId();			
			sessionManager.authorize(requestToken, authorizingAccountId, verifier);
		} catch (InvalidRequestTokenException e) {
			throw new InvalidOAuthTokenException(e.getMessage());
		}
	}
	
	public OAuthAccessProviderToken createAccessToken(String requestToken) {
		try {
			return providerTokenFor(sessionManager.grantAccess(requestToken));
		} catch (InvalidRequestTokenException e) {
			throw new InvalidOAuthTokenException(e.getMessage());
		}
	}

	public OAuthProviderToken getToken(String tokenValue) {
		try {
			return providerTokenFor(sessionManager.getSession(tokenValue));
		} catch (InvalidRequestTokenException e) {
			try {
				return providerTokenFor(accountRepository.findAppConnection(tokenValue));
			} catch (InvalidAccessTokenException ex) {
				throw new InvalidOAuthTokenException("Could not find OAuthSession or AppConnection for provided OAuth request token " + tokenValue);
			}
		}
	}
	
	// internal helpers
	
	private OAuthProviderToken providerTokenFor(OAuthSession session) {
		return new OAuthSessionProviderToken(session);
	}
	
	private OAuthAccessProviderToken providerTokenFor(AppConnection connection) {
		return new AppConnectionProviderToken(connection, accountRepository);
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
	private static class AppConnectionProviderToken implements OAuthAccessProviderToken {

		private AppConnection connection;

		private AccountRepository accountRepository;
		
		private Authentication userAuthentication;
		
		public AppConnectionProviderToken(AppConnection connection, AccountRepository accountRepository) {
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