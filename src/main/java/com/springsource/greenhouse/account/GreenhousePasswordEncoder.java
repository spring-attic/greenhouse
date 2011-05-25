/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.account;

import static org.springframework.security.crypto.util.EncodingUtils.concatenate;
import static org.springframework.security.crypto.util.EncodingUtils.subArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * A PasswordEncoder implementation that uses SHA-256 hashing implemented by the Sun provider with 8-byte random salting by default.
 * Compatibility note: this implementation does NOT perform digest iterations e.g. 1024 iterations.
 * This is for compatibility with the existing password database.
 * See {@link StandardPasswordEncoder} for an implementation that does perform digest iteration correctly (which makes its digests more secure).
 * @author Keith Donald
 * @see StandardPasswordEncoder
 */
public class GreenhousePasswordEncoder implements PasswordEncoder {

	private final Digester digester;

	private final byte[] secret;

	private final BytesKeyGenerator saltGenerator;

	/**
	 * Constructs a standard password encoder.
	 * @param secret the secret key used in the encoding process
	 */
	public GreenhousePasswordEncoder(String secret) {
		this("SHA-256", "SUN", secret);
	}

	/**
	 * Creates a fully customized standard password encoder.
	 */
	public GreenhousePasswordEncoder(String algorithm, String provider, String secret) {
		this.digester = new Digester(algorithm, provider);
		this.secret = Utf8.encode(secret);
		this.saltGenerator = KeyGenerators.secureRandom();
	}

	public String encode(CharSequence rawPassword) {
		return encode(rawPassword, saltGenerator.generateKey());
	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		byte[] digested = decode(encodedPassword);
		byte[] salt = subArray(digested, 0, saltGenerator.getKeyLength());
		return matches(digested, digest(rawPassword, salt));
	}

	// internal helpers

	private String encode(CharSequence rawPassword, byte[] salt) {
		byte[] digest = digest(rawPassword, salt);
		return new String(Hex.encode(digest));
	}

	private byte[] digest(CharSequence rawPassword, byte[] salt) {
		byte[] digest = digester.digest(concatenate(salt, secret, Utf8.encode(rawPassword)));
		return concatenate(salt, digest);
	}

	private byte[] decode(String encodedPassword) {
		return Hex.decode(encodedPassword);		
	}

	private boolean matches(byte[] expected, byte[] actual) {
		return Arrays.equals(expected, actual);
	}

	private static class Digester {

		private final MessageDigest messageDigest;

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
			// at least 1024 iterations should be applied here for additional security against brute-force attacks.
			// Unfortunately this was not done when the password database was populated.
			// Thus, we need to preserve compatible digest behavior.
			synchronized (messageDigest) {
				return messageDigest.digest(value);
			}
		}

	}
	
}
