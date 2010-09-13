package com.springsource.greenhouse.oauth.provider;

public interface OAuthSession {
	
	String getApiKey();

	String getCallbackUrl();
	
	String getRequestToken();
	
	String getSecret();

	boolean userAuthorized();
	
	Object getAuthorizingUser();

	String getVerifier();

}
