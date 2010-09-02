package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public final class AccountAlreadyConnectedException extends Exception {

	private final String provider;
	
	private final String accountId;

	public AccountAlreadyConnectedException(String provider, String accountId) {
		super("account already connected");
		this.provider = provider;
		this.accountId = accountId;
	}

	public String getProvider() {
		return provider;
	}
	
	public String getAccountId() {
		return accountId;
	}
	
}
