package com.springsource.greenhouse.oauth.provider;

import com.springsource.greenhouse.account.AppConnection;

public interface OAuthSessionManager {
	
	OAuthSession newOAuthSession(String apiKey, String callbackUrl);

	OAuthSession getSession(String requestToken) throws InvalidRequestTokenException;
	
	OAuthSession authorize(String requestToken, Long authorizingAccountId, String verifier) throws InvalidRequestTokenException;
	
	AppConnection grantAccess(String requestToken) throws InvalidRequestTokenException;
	
}
