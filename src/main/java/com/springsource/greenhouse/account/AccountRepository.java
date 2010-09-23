package com.springsource.greenhouse.account;

public interface AccountRepository {

	Account createAccount(Person person) throws EmailAlreadyOnFileException;

	Account authenticate(String username, String password) throws UsernameNotFoundException, InvalidPasswordException;

	void changePassword(Long accountId, String password);
	
	Account findById(Long accountId);

	Account findByUsername(String username) throws UsernameNotFoundException;

	void markProfilePictureSet(Long accountId);

}