package org.springframework.security.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Digester {

	private MessageDigest messageDigest;

	private int iterations = 1000;
	
	public Digester(String algorithm, String provider) {
		try {
			messageDigest = MessageDigest.getInstance(algorithm, provider);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No such hashing algorithm", e);
		} catch (NoSuchProviderException e) {
			throw new IllegalStateException("No such provider for hashing algorithm", e);
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