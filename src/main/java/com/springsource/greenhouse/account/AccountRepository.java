package com.springsource.greenhouse.account;

import java.util.List;

public interface AccountRepository {

	Account createAccount(Person person) throws EmailAlreadyOnFileException;

	Account authenticate(String username, String password) throws UsernameNotFoundException, InvalidPasswordException;

	void changePassword(Long accountId, String password);
	
	Account findById(Long accountId);

	Account findByUsername(String username) throws UsernameNotFoundException;

	void markProfilePictureSet(Long accountId);

	// account connection operations
	
	// TODO should we allow the user to connect again and overwrite previous connection details? this would be consistent with connectApp but does it make sense?
	void connectAccount(Long accountId, String provider, String accessToken, String providerAccountId) throws AccountConnectionAlreadyExists;

	boolean hasAccountConnection(Long accountId, String provider);

	Account findByAccountConnection(String provider, String accessToken) throws InvalidAccessTokenException;

	void disconnectAccount(Long accountId, String provider);

	List<Account> findFriendAccounts(String provider, List<String> providerFriendAccountIds);
	
	// app connection operations
	
	AppConnection connectApp(Long accountId, String apiKey) throws InvalidApiKeyException;

	AppConnection findAppConnection(String accessToken) throws InvalidAccessTokenException;
	
	void disconnectApp(Long accountId, String accessToken);

}