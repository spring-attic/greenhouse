package com.springsource.greenhouse.account;

public class ShaHashedRandomSaltedPasswordEncoder implements PasswordEncoder {

	public String encode(String rawPassword) {
		return rawPassword;
	}

	public boolean matches(String rawPassword, String encodedPassword) {
		return rawPassword.equals(encodedPassword);
	}

}
