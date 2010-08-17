package com.springsource.greenhouse.account;

import java.util.List;

public interface AccountRepository {

	Account findById(Long id);

	Account findByUsername(String username) throws UsernameNotFoundException;
	
	Account findByConnectedAccount(String provider, String accessToken) throws ConnectedAccountNotFoundException;

	List<Account> findFriendAccounts(String provider, List<String> friendIds);
	
	void connect(Long id, String provider, String accessToken, String accountId) throws AccountAlreadyConnectedException;

	boolean isConnected(Long id, String provider);

	void disconnect(Long id, String provider);
		
}
