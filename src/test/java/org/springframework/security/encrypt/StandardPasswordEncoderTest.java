package org.springframework.security.encrypt;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.encrypt.EncodingUtils.concatenate;
import static org.springframework.security.encrypt.EncodingUtils.hexEncode;
import static org.springframework.security.encrypt.EncodingUtils.hexDecode;
import static org.springframework.security.encrypt.EncodingUtils.utf8Encode;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.security.encrypt.SecureRandomSaltGenerator;
import org.springframework.security.encrypt.StandardPasswordEncoder;

public class StandardPasswordEncoderTest {

	StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder("SHA-256", "secret");

	private byte[] secret = utf8Encode("secret");
	
	@Test
	public void encodePassword() {
		String encodedPassword = passwordEncoder.encode("melbourne");
 		assertTrue(passwordEncoder.matches("melbourne", encodedPassword));
	}
	
	@Test
	public void test() {
		SecureRandomSaltGenerator generator = new SecureRandomSaltGenerator("SHA1PRNG");
		byte[] salt = generator.generateSalt(8);
		assertTrue(Arrays.equals(concat1("test", salt), concat2("test", salt)));
		
		byte[] bytes = concat2("test", salt);
		String encoded = hexEncode(bytes);
		assertTrue(Arrays.equals(bytes, hexDecode(encoded)));
	}
	
	private byte[] concat1(String rawPassword, byte[] salt) {
		byte[] secreted = concatenate(salt, secret);
		return concatenate(secreted, utf8Encode(rawPassword));
	}
	
	private byte[] concat2(String rawPassword, byte[] salt) {
		return concatenate(salt, secret, utf8Encode(rawPassword));
	}
	
	
	
}
