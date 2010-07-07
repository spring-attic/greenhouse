package com.springsource.greenhouse.signin.password;

public class PasswordResetMessage {
	private final String resetRequestId;
	private final String email;

	public PasswordResetMessage(String resetRequestId, String email) {
		this.resetRequestId = resetRequestId;
		this.email = email;
	}
	
	public String getResetRequestId() {
    	return resetRequestId;
    }

	public String getEmail() {
    	return email;
    }
}
