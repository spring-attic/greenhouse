package org.springframework.security.encrypt;

import static org.springframework.security.encrypt.EncodingUtils.hexDecode;

public final class SiteGlobalSaltFactory implements SaltFactory {

	private byte[] salt;

	public SiteGlobalSaltFactory(String salt) {
		this.salt = hexDecode(salt);
	}

    public int getSaltLength() {
    	return salt.length;
    }
    
    public boolean isRandomSalt() {
    	return false;
    }
    
	public byte[] getSalt() {
		return salt;
	}

}
