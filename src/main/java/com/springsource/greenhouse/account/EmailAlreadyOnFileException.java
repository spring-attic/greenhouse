package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public class EmailAlreadyOnFileException extends AccountException {
	
	private String email;
	
	public EmailAlreadyOnFileException(String email) {
		super("email already on file");
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
}
