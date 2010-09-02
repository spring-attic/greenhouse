package org.springframework.security.password;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class StandardPasswordEncoder implements PasswordEncoder {

	private MessageDigest messageDigest;

	private String secret;
	
	public StandardPasswordEncoder(String algorithm, String secret) {
		this.secret = secret;
		try {
			messageDigest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Should not happen", e);
		}
	}

	public String encode(String rawPassword) {
		return encode(rawPassword, UUID.randomUUID().toString());
	}

	public boolean matches(String rawPassword, String encodedPassword) {
		return encode(rawPassword, encodedPassword.split(":")[1]).equals(encodedPassword);
	}

	private String encode(String rawPassword, String salt) {
		return (hash(hash(salt + secret) + rawPassword) + ":" + salt);		
	}
	
	private String hash(String value) {
		try {
			return hexEncode(messageDigest.digest(value.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 not supported!");
		}
	}

	static private String hexEncode(byte[] aInput) {
		StringBuilder result = new StringBuilder();
		char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		for (int idx = 0; idx < aInput.length; ++idx) {
			byte b = aInput[idx];
			result.append(digits[(b & 0xf0) >> 4]);
			result.append(digits[b & 0x0f]);
		}
		return result.toString();
	}

}