package com.springsource.greenhouse.develop.oauth;

public interface OAuthSession {
	
	String getApiKey();

	String getCallbackUrl();
	
	String getRequestToken();
	
	String getSecret();

	boolean authorized();
	
	Long getAuthorizingAccountId();

	String getVerifier();

}
