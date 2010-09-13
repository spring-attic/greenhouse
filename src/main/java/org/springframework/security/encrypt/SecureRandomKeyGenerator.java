package org.springframework.security.encrypt;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public final class SecureRandomKeyGenerator implements KeyGenerator {

	private final SecureRandom random;

    private final int keyLength;
    
    public SecureRandomKeyGenerator() {
    	this(DEFAULT_ALGORITHM, DEFAULT_PROVIDER, DEFAULT_KEY_LENGTH);
    }
    
    public SecureRandomKeyGenerator(int keyLength) {
    	this(DEFAULT_ALGORITHM, DEFAULT_PROVIDER, keyLength);    	
    }
    
    public SecureRandomKeyGenerator(String algorithm, String provider, int keyLength) {
        this.random = createSecureRandom(algorithm, provider, keyLength);
        this.keyLength = keyLength;
    }
   
    public int getKeyLength() {
    	return keyLength;
    }
    
	public byte[] generateKey() {
        byte[] bytes = new byte[keyLength];
        random.nextBytes(bytes);
        return bytes;
	}
	
	private SecureRandom createSecureRandom(String algorithm, String provider, int keyLength) {
		try {
            SecureRandom random = SecureRandom.getInstance(algorithm, provider);
            random.setSeed(random.generateSeed(keyLength));
            return random;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Not a supported SecureRandom key generation algorithm", e);
        } catch (NoSuchProviderException e) {
            throw new IllegalArgumentException("Not a supported SecureRandom key provider", e);
		}
	}
	
	private static final String DEFAULT_ALGORITHM = "SHA1PRNG";

	private static final String DEFAULT_PROVIDER = "SUN";

    private static final int DEFAULT_KEY_LENGTH = 8;

}
