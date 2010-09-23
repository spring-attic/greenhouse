package com.springsource.greenhouse.develop.oauth;

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