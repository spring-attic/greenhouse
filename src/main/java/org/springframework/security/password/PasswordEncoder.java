package org.springframework.security.password;

public interface PasswordEncoder {
	
	String encode(String rawPassword);
	
	boolean matches(String rawPassword, String encodedPassword);
	
}
