/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.develop.oauth;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.provider.token.InvalidOAuthTokenException;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.stereotype.Service;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.develop.AppConnection;
import com.springsource.greenhouse.develop.AppRepository;
import com.springsource.greenhouse.develop.NoSuchAccountConnectionException;

/**
 * Adapts the {@link OAuthSessionManager} API to the Spring Security {@link OAuthProviderTokenServices}.
 * Allows for the {@link OAuthSessionManager} to be used with Spring Security OAuth-based Provider to store OAuth request state and establish OAuth connections.
 * @author Keith Donald
 */
@Service("oauthProviderTokenServices")
public class OAuthSessionManagerProviderTokenServices implements OAuthProviderTokenServices {
	
	private OAuthSessionManager sessionManager;

	private AccountRepository accountRepository;
	
	private AppRepository appRepository;
	
	@Inject
	public OAuthSessionManagerProviderTokenServices(OAuthSessionManager sessionManager, AccountRepository accountRepository, AppRepository appRepository) {
		this.sessionManager = sessionManager;
		this.accountRepository = accountRepository;
		this.appRepository = appRepository;
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

	// TODO the fact Spring Security OAuth does not distinguish between requests for a OAuthSession indexed by a requestToken
	// and requests for an AppConnection indexed by an accessToken always forces us to check in both places, which is less than ideal.  
	public OAuthProviderToken getToken(String tokenValue) {
		try {
			return providerTokenFor(sessionManager.getSession(tokenValue));
		} catch (InvalidRequestTokenException e) {
			try {
				return providerTokenFor(appRepository.findAppConnection(tokenValue));
			} catch (NoSuchAccountConnectionException ex) {
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

}