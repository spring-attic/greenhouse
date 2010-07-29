package com.springsource.greenhouse.account;

public interface AccountRepository {

	Account findAccount(Long id);

	Account findAccount(String username) throws UsernameNotFoundException;
	
	Account findByConnectedAccount(String accessToken, String accountName) throws ConnectedAccountNotFoundException;
	
	void removeConnectedAccount(Long memberId, String accountName);
	
	void connectAccount(Long memberId, String externalId, String accountName, String accessToken, String secret);

	boolean isConnected(Long memberId, String accountName);
}
