package org.springframework.security.encrypt;

public interface SaltGenerator {
	
	byte[] generateSalt(int length);
	
}
