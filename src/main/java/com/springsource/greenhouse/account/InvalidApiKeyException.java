package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public class InvalidApiKeyException extends Exception {

	private String apiKey;
	
	public InvalidApiKeyException(String apiKey) {
		super("invalid api key " + apiKey);
		this.apiKey = apiKey;
	}
	
	public String getApiKey() {
		return apiKey;
	}

}
