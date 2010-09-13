package com.springsource.greenhouse.oauth.provider;

@SuppressWarnings("serial")
public class InvalidRequestTokenException extends Exception {

	private String requestToken;

	public InvalidRequestTokenException(String requestToken) {
		super("invalid request token");
		this.requestToken = requestToken;
	}
	
	public String getRequestToken() {
		return requestToken;
	}
	 
}