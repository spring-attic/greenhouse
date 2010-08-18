package com.springsource.greenhouse.account;

public interface PasswordEncoder {
	
	String encode(String rawPassword);
	
	boolean matches(String rawPassword, String encodedPassword);
	
}
