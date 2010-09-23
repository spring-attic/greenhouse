package com.springsource.greenhouse.account;

public interface AccountRepository {

	Account createAccount(Person person) throws EmailAlreadyOnFileException;

	Account authenticate(String username, String password) throws UsernameNotFoundException, InvalidPasswordException;

	void changePassword(Long accountId, String password);
	
	Account findById(Long accountId);

	Account findByUsername(String username) throws UsernameNotFoundException;

	void markProfilePictureSet(Long accountId);

	// account connection operations
	
	// TODO should we allow the user to connect again and overwrite previous connection details? this would be consistent with connectApp but does it make sense?
	Account findByAccountConnection(String provider, String accessToken) throws InvalidAccessTokenException;
	
}