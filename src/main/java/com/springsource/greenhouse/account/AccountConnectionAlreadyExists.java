package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public final class AccountConnectionAlreadyExists extends AccountException {

	private final String provider;
	
	private final String accountId;

	public AccountConnectionAlreadyExists(String provider, String accountId) {
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
