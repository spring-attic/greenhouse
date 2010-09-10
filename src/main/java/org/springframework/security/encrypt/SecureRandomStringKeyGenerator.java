package org.springframework.security.encrypt;

import static org.springframework.security.encrypt.EncodingUtils.hexEncode;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class SecureRandomStringKeyGenerator implements StringKeyGenerator {

    private final SecureRandom random;

    private final int keyLength = 8;
    
    public SecureRandomStringKeyGenerator(String algorithm) {
        try {
            this.random = SecureRandom.getInstance(algorithm);
            this.random.setSeed(System.currentTimeMillis());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Not a supported SecureRandom salt generation algorithm", e);
        }
    }
    
	public String generateKey() {
        byte[] bytes = new byte[keyLength];
        random.nextBytes(bytes);
        return hexEncode(bytes);
	}

}
