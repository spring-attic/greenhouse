package com.springsource.greenhouse.reset;

import com.springsource.greenhouse.account.UsernameNotFoundException;

public interface ResetPasswordService {

	void sendResetMail(String username) throws UsernameNotFoundException;

	boolean isValidResetToken(String token);

	void changePassword(String token, String password) throws InvalidResetTokenException;
	
}
