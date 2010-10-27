package com.springsource.greenhouse.reset;

import com.springsource.greenhouse.account.SignInNotFoundException;

public interface ResetPasswordService {

	void sendResetMail(String username) throws SignInNotFoundException;

	boolean isValidResetToken(String token);

	void changePassword(String token, String password) throws InvalidResetTokenException;
	
}
