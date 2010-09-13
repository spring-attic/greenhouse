package com.springsource.greenhouse.oauth.provider;

import com.springsource.greenhouse.account.ConnectedApp;

public interface OAuthSessionManager {
	
	OAuthSession newOAuthSession(String apiKey, String callbackUrl);

	OAuthSession getSession(String requestToken);
	
	void authorize(String requestToken, Long authorizingAccountId, String verifier);
	
	ConnectedApp grantAccess(String requestToken);
	
}
