package org.springframework.security.encrypt;

import static org.springframework.security.encrypt.EncodingUtils.concatenate;
import static org.springframework.security.encrypt.EncodingUtils.hexDecode;
import static org.springframework.security.encrypt.EncodingUtils.hexEncode;
import static org.springframework.security.encrypt.EncodingUtils.subArray;
import static org.springframework.security.encrypt.EncodingUtils.utf8Encode;

import java.util.Arrays;

public class StandardPasswordEncoder implements PasswordEncoder {

	private Digester digester;

	private byte[] secret;

	private SecureRandomSaltFactory saltFactory;

	public StandardPasswordEncoder(String secret) {
		this("SHA-256", "SUN", secret);
	}
	
	public StandardPasswordEncoder(String algorithm, String provider, String secret) {
		this.digester = new Digester(algorithm, provider);
		this.secret = utf8Encode(secret);
		this.saltFactory = new SecureRandomSaltFactory();
	}

	public String encode(String rawPassword) {
		return encode(rawPassword, nextRandomSalt());
	}

	public boolean matches(String rawPassword, String encodedPassword) {
		byte[] digested = decode(encodedPassword);
		byte[] salt = subArray(digested, 0, saltFactory.getSaltLength());
		return matches(digested, digest(rawPassword, salt));
	}

	// internal helpers

	private byte[] nextRandomSalt() {
		return saltFactory.getSalt();
	}

	private String encode(String rawPassword, byte[] salt) {
		byte[] digest = digest(rawPassword, salt);
		return hexEncode(digest);
	}

	private byte[] digest(String rawPassword, byte[] salt) {
		byte[] digest = digester.digest(concatenate(salt, secret, utf8Encode(rawPassword)));
		return concatenate(salt, digest);
	}
	
	private byte[] decode(String encodedPassword) {
		return hexDecode(encodedPassword);		
	}
	
	private boolean matches(byte[] expected, byte[] actual) {
		return Arrays.equals(expected, actual);
	}

}