package com.springsource.greenhouse.signin.password;

public class ResetPasswordMessage {
	private final String requestKey;
	private final String email;

	public ResetPasswordMessage(String requestKey, String email) {
		this.requestKey = requestKey;
		this.email = email;
	}
	
	public String getRequestKey() {
    	return requestKey;
    }

	public String getEmail() {
    	return email;
    }
}
