package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public class UsernameNotFoundException extends AccountException {

	private String username;
	
	public UsernameNotFoundException(String username) {
		super("username not found");
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}

}
