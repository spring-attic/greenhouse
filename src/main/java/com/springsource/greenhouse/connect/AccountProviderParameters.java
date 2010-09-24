package com.springsource.greenhouse.connect;

import org.springframework.web.util.UriTemplate;

class AccountProviderParameters {
	
	private final String name;
	
	private final String apiKey;
	
	private final String secret;
	
	private final Long appId;
	
	private final String requestTokenUrl;
	
	private final UriTemplate authorizeUrl;
	
	private final String callbackUrl;
	
	private final String accessTokenUrl;

	public AccountProviderParameters(String name, String apiKey, String secret, Long appId, String requestTokenUrl, String authorizeUrl, String callbackUrl, String accessTokenUrl) {
		this.name = name;
		this.apiKey = apiKey;
		this.secret = secret;
		this.appId = appId;
		this.requestTokenUrl = requestTokenUrl;
		this.authorizeUrl = authorizeUrl != null ? new UriTemplate(authorizeUrl) : null;
		this.callbackUrl = callbackUrl;
		this.accessTokenUrl = accessTokenUrl;
	}

	public String getName() {
		return name;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getSecret() {
		return secret;
	}

	public Long getAppId() {
		return appId;
	}

	public String getRequestTokenUrl() {
		return requestTokenUrl;
	}

	public UriTemplate getAuthorizeUrl() {
		return authorizeUrl;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}
	
}