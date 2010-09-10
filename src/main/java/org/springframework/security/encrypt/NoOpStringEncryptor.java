package org.springframework.security.encrypt;

public class NoOpStringEncryptor implements StringEncryptor {

	public String encrypt(String string) {
		return string;
	}

	public String decrypt(String encrypted) {
		return encrypted;
	}

	public static StringEncryptor getInstance() {
		return INSTANCE;
	}
	
	private static final StringEncryptor INSTANCE = new NoOpStringEncryptor();
	
	private NoOpStringEncryptor() {
		
	}
}
