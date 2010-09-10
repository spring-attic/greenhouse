package org.springframework.security.encrypt;

public interface PasswordEncoder {
	
	String encode(String rawPassword);
	
	boolean matches(String rawPassword, String encodedPassword);
	
}
