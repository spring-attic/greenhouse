package org.springframework.security.encrypt;

import static org.springframework.security.encrypt.EncodingUtils.hexEncode;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public final class SecureRandomStringKeyGenerator implements StringKeyGenerator {

    private final SecureRandom random;

    private final int keyLength;
    
    public SecureRandomStringKeyGenerator() {
    	this("SHA1PRNG", "SUN", 8);
    }
    
    public SecureRandomStringKeyGenerator(String algorithm, String provider, int keyLength) {
        this.random = createSecureRandom(algorithm, provider, keyLength);
        this.keyLength = keyLength;
    }
   
	public String generateKey() {
        byte[] bytes = new byte[keyLength];
        random.nextBytes(bytes);
        return hexEncode(bytes);
	}
	
	private SecureRandom createSecureRandom(String algorithm, String provider, int keyLength) {
		try {
            SecureRandom random = SecureRandom.getInstance(algorithm, provider);
            random.setSeed(random.generateSeed(keyLength));
            return random;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Not a supported SecureRandom generation algorithm", e);
        } catch (NoSuchProviderException e) {
            throw new IllegalArgumentException("Not a supported SecureRandom provider", e);
		}
	}

}
