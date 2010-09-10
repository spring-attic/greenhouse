package com.springsource.greenhouse.develop;

public class App {
	
	private final AppSummary summary;

	private final String apiKey;
	
	private final String secret;
	
	private final String callbackUrl;

	public App(AppSummary summary, String apiKey, String secret, String callbackUrl) {
		this.summary = summary;
		this.apiKey = apiKey;
		this.secret = secret;
		this.callbackUrl = callbackUrl;
	}

	public AppSummary getSummary() {
		return summary;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getSecret() {
		return secret;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

}
