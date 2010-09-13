package com.springsource.greenhouse.oauth.provider;

import com.springsource.greenhouse.account.ConnectedApp;

public interface OAuthSessionManager {
	
	OAuthSession newOAuthSession(String apiKey, String callbackUrl);

	OAuthSession getSession(String requestToken);
	
	void authorizeRequestToken(String requestToken, Object userPrincipal, String verifier);
	
	ConnectedApp grantAccess(String requestToken);
	
}
