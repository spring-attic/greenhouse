package com.springsource.greenhouse.reset;

import com.springsource.greenhouse.account.Account;

public class ResetPasswordRequest {
	
	private String token;
	
	private Account account;

	public ResetPasswordRequest(String token, Account account) {
		this.token = token;
		this.account = account;
	}

	public String getToken() {
		return token;
	}

	public Account getAccount() {
		return account;
	}
		
}
