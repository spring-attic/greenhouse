package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public final class ConnectedAccountNotFoundException extends AccountException {

	private final String provider;
	
	public ConnectedAccountNotFoundException(String provider) {
		super("connected account not found");
		this.provider = provider;
	}

	public String getProvider() {
		return provider;
	}

}
