/*
 * Copyright 2010 the original author or authors.
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
package org.springframework.security.encrypt;

import static org.springframework.security.encrypt.EncodingUtils.concatenate;
import static org.springframework.security.encrypt.EncodingUtils.hexDecode;
import static org.springframework.security.encrypt.EncodingUtils.hexEncode;
import static org.springframework.security.encrypt.EncodingUtils.subArray;
import static org.springframework.security.encrypt.EncodingUtils.utf8Encode;

import java.util.Arrays;

/**
 * @author Keith Donald
 */
public class StandardPasswordEncoder implements PasswordEncoder {

	private Digester digester;

	private byte[] secret;

	private SecureRandomKeyGenerator saltGenerator;

	public StandardPasswordEncoder(String secret) {
		this("SHA-256", "SUN", secret);
	}
	
	public StandardPasswordEncoder(String algorithm, String provider, String secret) {
		this.digester = new Digester(algorithm, provider);
		this.secret = utf8Encode(secret);
		this.saltGenerator = new SecureRandomKeyGenerator();
	}

	public String encode(String rawPassword) {
		return encode(rawPassword, saltGenerator.generateKey());
	}

	public boolean matches(String rawPassword, String encodedPassword) {
		byte[] digested = decode(encodedPassword);
		byte[] salt = subArray(digested, 0, saltGenerator.getKeyLength());
		return matches(digested, digest(rawPassword, salt));
	}

	// internal helpers
	
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