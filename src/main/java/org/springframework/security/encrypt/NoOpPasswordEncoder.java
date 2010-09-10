package org.springframework.security.encrypt;

public class NoOpPasswordEncoder implements PasswordEncoder {

	public String encode(String rawPassword) {
		return rawPassword;
	}

	public boolean matches(String rawPassword, String encodedPassword) {
		return rawPassword.equals(encodedPassword);
	}
	
	public static PasswordEncoder getInstance() {
		return INSTANCE;
	}
	
	private static final PasswordEncoder INSTANCE = new NoOpPasswordEncoder();
	
	private NoOpPasswordEncoder() {
		
	}

}
