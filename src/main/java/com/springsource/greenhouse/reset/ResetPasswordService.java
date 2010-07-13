package com.springsource.greenhouse.reset;

import com.springsource.greenhouse.account.AccountNotFoundException;

public interface ResetPasswordService {

	void sendResetMail(String username) throws AccountNotFoundException;

	boolean isValidResetToken(String token);

	void changePassword(String token, String password) throws InvalidTokenException;
	
}
