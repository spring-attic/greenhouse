package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public final class UsernameNotFoundException extends AccountException {

	private final String username;
	
	public UsernameNotFoundException(String username) {
		super("username not found");
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}

}
