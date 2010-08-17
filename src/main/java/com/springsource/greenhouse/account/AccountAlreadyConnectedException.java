package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public class AccountAlreadyConnectedException extends Exception {

	private String accountId;

	public AccountAlreadyConnectedException(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}
	
}
