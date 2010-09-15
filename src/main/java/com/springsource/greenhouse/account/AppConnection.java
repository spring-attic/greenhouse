package com.springsource.greenhouse.account;

public final class AppConnection {

	private final Long accountId;
	
	private final String apiKey;

	private final String accessToken;
	
	private final String secret;
	
	public AppConnection(Long accountId, String apiKey, String accessToken, String secret) {
		this.accountId = accountId;
		this.apiKey = apiKey;
		this.accessToken = accessToken;
		this.secret = secret;
	}

	public Long getAccountId() {
		return accountId;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public String getSecret() {
		return secret;
	}

}
