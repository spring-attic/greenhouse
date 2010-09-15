package org.springframework.security.encrypt;

public interface KeyGenerator {

	public int getKeyLength();
	
	byte[] generateKey();
	
}
