package org.springframework.security.encrypt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SearchableStringEncryptorTests {
	
	private SearchableStringEncryptor encryptor = new SearchableStringEncryptor("secret", "5b8bd7612cdab5ed");
	
	@Test
	public void encrypt() {
		String cipherText = encryptor.encrypt("123456789");
		String plainText = encryptor.decrypt(cipherText);
		assertEquals("123456789", plainText);
	}
}
