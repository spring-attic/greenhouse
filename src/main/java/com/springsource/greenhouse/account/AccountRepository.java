package com.springsource.greenhouse.account;

public interface AccountRepository {

	Account findById(Long id);

	Account findByUsername(String username) throws UsernameNotFoundException;
	
	Account findByConnectedAccount(String accountName, String accessToken) throws ConnectedAccountNotFoundException;

	void connect(Long id, String accountName, String accessToken);

	boolean isConnected(Long id, String accountName);

	void disconnect(Long id, String accountName);
		
}
