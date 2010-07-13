package com.springsource.greenhouse.account;

public interface UsernamePasswordAuthenticationService {

	Account authenticate(String username, String password) throws AccountNotFoundException, InvalidPasswordException;

}
