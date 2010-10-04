package com.springsource.greenhouse.connect;

import org.springframework.web.util.UriTemplate;

class AccountProviderParameters {
	
	private final String name;
	
	private final String displayName;

	private final String apiKey;
	
	private final String secret;
	
	private final Long appId;
	
	private final String requestTokenUrl;
	
	private final UriTemplate authorizeUrl;
	
	private final String accessTokenUrl;

	public AccountProviderParameters(String name, String displayName, String apiKey, String secret, Long appId,
			String requestTokenUrl, String authorizeUrl, String accessTokenUrl) {
		this.name = name;
		this.displayName = displayName;
		this.apiKey = apiKey;
		this.secret = secret;
		this.appId = appId;
		this.requestTokenUrl = requestTokenUrl;
		this.authorizeUrl = authorizeUrl != null ? new UriTemplate(authorizeUrl) : null;
		this.accessTokenUrl = accessTokenUrl;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
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

	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}
	
}