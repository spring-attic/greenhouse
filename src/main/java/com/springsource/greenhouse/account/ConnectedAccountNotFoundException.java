package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public final class ConnectedAccountNotFoundException extends AccountException {

	private final String provider;
	
	public ConnectedAccountNotFoundException(String provider) {
		super("account not connected to provider " + provider);
		this.provider = provider;
	}

	public String getProvider() {
		return provider;
	}

}
