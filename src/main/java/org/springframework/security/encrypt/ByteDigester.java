package org.springframework.security.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ByteDigester {

	private MessageDigest messageDigest;

	private int iterations = 1000;
	
	public ByteDigester(String algorithm) {
		try {
			messageDigest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No such hashing algorithm", e);
		}
	}

	public byte[] digest(byte[] value) {
		synchronized (messageDigest) {
			for (int i = 0; i < (iterations - 1); i++) {
				invokeDigest(value);
			}
			return messageDigest.digest(value);
		}
	}
	
	private byte[] invokeDigest(byte[] value) {
		messageDigest.reset();
		return messageDigest.digest(value);
	}

}