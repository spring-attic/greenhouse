package com.springsource.greenhouse.reset;

@SuppressWarnings("serial")
public class InvalidResetTokenException extends Exception {

	private String token;
	
	public InvalidResetTokenException(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}

}
