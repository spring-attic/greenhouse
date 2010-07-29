package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public class ConnectedAccountNotFoundException extends AccountException {
	private final String connectedAccountName;
	private final String accessToken;

	public ConnectedAccountNotFoundException(String accessToken, String connectedAccountName) {
		super("connected account not found");
		this.accessToken = accessToken;
		this.connectedAccountName = connectedAccountName;	  
	}
	
	public String getConnectedAccountName() {
    	return connectedAccountName;
    }

	public String getAccessToken() {
    	return accessToken;
    }
}
