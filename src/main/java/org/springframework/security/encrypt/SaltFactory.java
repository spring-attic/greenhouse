package org.springframework.security.encrypt;

public interface SaltFactory {

	int getSaltLength();

	boolean isRandomSalt();
	
	byte[] getSalt();
	
}
