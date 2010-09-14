package org.springframework.security.encrypt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.Test;

public class SecureRandomKeyGeneratorTests {
	
	private SecureRandomKeyGenerator generator = new SecureRandomKeyGenerator();
	
	@Test
	public void generateKey() {
		byte[] bytes = generator.generateKey();
		assertEquals(8, bytes.length);
		assertFalse(Arrays.equals(bytes, generator.generateKey()));
	}
}
