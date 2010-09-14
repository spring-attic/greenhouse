package org.springframework.security.encrypt;

import static org.springframework.security.encrypt.EncodingUtils.hexEncode;

public final class SecureRandomStringKeyGenerator implements StringKeyGenerator {

	private final SecureRandomKeyGenerator keyGenerator;
    
    public SecureRandomStringKeyGenerator() {
    	keyGenerator = new SecureRandomKeyGenerator();
    }
    
    public SecureRandomStringKeyGenerator(String algorithm, String provider, int keyLength) {
    	keyGenerator = new SecureRandomKeyGenerator(algorithm, provider, keyLength);
    }
   
	public String generateKey() {
        return hexEncode(keyGenerator.generateKey());
	}

}
