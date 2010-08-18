package com.springsource.greenhouse.account;

import java.util.List;

public interface AccountRepository {

	Account createAccount(Person person) throws EmailAlreadyOnFileException;

	Account authenticate(String username, String password) throws UsernameNotFoundException, InvalidPasswordException;

	Account findById(Long id);

	Account findByUsername(String username) throws UsernameNotFoundException;
	
	Account findByConnectedAccount(String provider, String accessToken) throws ConnectedAccountNotFoundException; // TODO exception case where accessToken is valid

	List<Account> findFriendAccounts(String provider, List<String> friendIds);
	
	void connect(Long id, String provider, String accessToken, String accountId) throws AccountAlreadyConnectedException;

	boolean isConnected(Long id, String provider);

	void disconnect(Long id, String provider);
		
}
