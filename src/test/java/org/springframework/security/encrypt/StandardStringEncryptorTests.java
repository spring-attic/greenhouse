package org.springframework.security.encrypt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class StandardStringEncryptorTests {
	
	private StandardStringEncryptor encryptor = new StandardStringEncryptor("secret");
	
	@Test
	public void encrypt() {
		String cipherText = encryptor.encrypt("123456789");
		String plainText = encryptor.decrypt(cipherText);
		assertEquals("123456789", plainText);

		String cipherText2 = encryptor.encrypt("123456789");
		assertFalse(cipherText.equals(cipherText2));
		assertEquals("123456789", encryptor.decrypt(cipherText));		
	}
}
