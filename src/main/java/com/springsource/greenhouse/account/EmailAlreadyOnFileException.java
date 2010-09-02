package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public class EmailAlreadyOnFileException extends Exception {
	
	private String email;
	
	public EmailAlreadyOnFileException(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
}
