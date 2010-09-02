package org.springframework.security.password;

public class NoOpPasswordEncoder implements PasswordEncoder {

	public String encode(String rawPassword) {
		return rawPassword;
	}

	public boolean matches(String rawPassword, String encodedPassword) {
		return rawPassword.equals(encodedPassword);
	}

}
