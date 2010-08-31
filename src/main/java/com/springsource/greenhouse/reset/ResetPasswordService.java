package com.springsource.greenhouse.reset;

import org.springframework.social.account.UsernameNotFoundException;

public interface ResetPasswordService {

	void sendResetMail(String username) throws UsernameNotFoundException;

	boolean isValidResetToken(String token);

	void changePassword(String token, String password) throws InvalidResetTokenException;
	
}
