package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public class AccountNotFoundException extends Exception {

	private String username;
	
	public AccountNotFoundException(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}

}
