package org.springframework.security.encrypt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class SecureRandomSaltGenerator implements SaltGenerator {

    private final SecureRandom random;

    public SecureRandomSaltGenerator(String algorithm) {
        try {
            this.random = SecureRandom.getInstance(algorithm);
            this.random.setSeed(System.currentTimeMillis());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Not a supported SecureRandom salt generation algorithm", e);
        }
    }
    
	public byte[] generateSalt(int length) {
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return salt;
	}

}
