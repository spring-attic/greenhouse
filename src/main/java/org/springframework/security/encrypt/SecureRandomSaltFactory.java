package org.springframework.security.encrypt;

public final class SecureRandomSaltFactory implements SaltFactory {

	private SecureRandomKeyGenerator saltGenerator;

	public SecureRandomSaltFactory() {
		saltGenerator = new SecureRandomKeyGenerator();
	}

	public SecureRandomSaltFactory(int saltLength) {
		saltGenerator = new SecureRandomKeyGenerator(saltLength);
	}

    public int getSaltLength() {
    	return saltGenerator.getKeyLength();
    }
    
    public boolean isRandomSalt() {
    	return true;
    }
    
	public byte[] getSalt() {
		return saltGenerator.generateKey();
	}

}
