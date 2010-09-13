package com.springsource.greenhouse.account;

import java.util.List;

public interface AccountRepository {

	Account createAccount(Person person) throws EmailAlreadyOnFileException;

	Account authenticate(String username, String password) throws UsernameNotFoundException, InvalidPasswordException;

	void changePassword(Long accountId, String password);
	
	Account findById(Long accountId);

	Account findByUsername(String username) throws UsernameNotFoundException;

	void markProfilePictureSet(Long accountId);

	// connected account operations
	
	Account findByConnectedAccount(String provider, String accessToken) throws ConnectedAccountNotFoundException;

	// TODO should we allow the user to connect again and overwrite previous connection details?
	void connectAccount(Long accountId, String provider, String accessToken, String providerAccountId) throws AccountAlreadyConnectedException;

	boolean hasConnectedAccount(Long accountId, String provider);

	void disconnectAccount(Long accountId, String provider);

	List<Account> findFriendAccounts(String provider, List<String> providerFriendAccountIds);
	
	// connected app operations
	
	ConnectedApp connectApp(Long accountId, String apiKey);

	// TODO - we would like to pass in apiKey here but unfortunately Spring Security OAuth does not make it available where this method is delegated to
	ConnectedApp findConnectedApp(String accessToken) throws ConnectedAppNotFoundException;

}