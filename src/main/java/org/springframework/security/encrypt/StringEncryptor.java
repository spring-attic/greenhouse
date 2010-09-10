package org.springframework.security.encrypt;

public interface StringEncryptor {

	String encrypt(String string);
	
	String decrypt(String encrypted);
	
}
