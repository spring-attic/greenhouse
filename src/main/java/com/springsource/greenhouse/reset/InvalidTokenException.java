package com.springsource.greenhouse.reset;

@SuppressWarnings("serial")
public class InvalidTokenException extends Exception {

	private String token;
	
	public InvalidTokenException(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}

}
