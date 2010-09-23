package com.springsource.greenhouse.develop;

import com.springsource.greenhouse.account.AccountException;

@SuppressWarnings("serial")
public class InvalidApiKeyException extends AccountException {

	private String apiKey;
	
	public InvalidApiKeyException(String apiKey) {
		super("invalid api key " + apiKey);
		this.apiKey = apiKey;
	}
	
	public String getApiKey() {
		return apiKey;
	}

}
