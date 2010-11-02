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

import static org.springframework.security.encrypt.CipherUtils.doFinal;
import static org.springframework.security.encrypt.CipherUtils.initCipher;
import static org.springframework.security.encrypt.CipherUtils.newCipher;
import static org.springframework.security.encrypt.CipherUtils.newSecretKey;
import static org.springframework.security.encrypt.EncodingUtils.concatenate;
import static org.springframework.security.encrypt.EncodingUtils.hexDecode;
import static org.springframework.security.encrypt.EncodingUtils.hexEncode;
import static org.springframework.security.encrypt.EncodingUtils.subArray;
import static org.springframework.security.encrypt.EncodingUtils.utf8Decode;
import static org.springframework.security.encrypt.EncodingUtils.utf8Encode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * @author Keith Donald
 */
// TODO evaluate AES for higher-level of security
public class StandardStringEncryptor implements StringEncryptor {

	private final SecretKey secretKey;

	private final KeyGenerator saltGenerator;
	
	private final Cipher encryptor;

	private final Cipher decryptor;

	public StandardStringEncryptor(String password) {
		String algorithm = "PBEWithMD5AndDES";		
		secretKey = newSecretKey(algorithm, password);
		saltGenerator = new SecureRandomKeyGenerator();
		encryptor = newCipher(algorithm);
		decryptor = newCipher(algorithm);
	}
	
	public String encrypt(String text) {
		byte[] salt = saltGenerator.generateKey();
		byte[] encrypted;
		synchronized (encryptor) {
			initCipher(encryptor, Cipher.ENCRYPT_MODE, secretKey, salt, 1000);
			encrypted = doFinal(encryptor, utf8Encode(text));
		}
		return hexEncode(concatenate(salt, encrypted));
	}

	public String decrypt(String encryptedText) {
		byte[] encrypted = hexDecode(encryptedText);
		byte[] salt = saltPart(encrypted);
		byte[] decrypted;
		synchronized (decryptor) {
			initCipher(decryptor, Cipher.DECRYPT_MODE, secretKey, salt, 1000);
			decrypted = doFinal(decryptor, cipherPart(encrypted, salt));
		}
		return utf8Decode(decrypted);
	}
	
	private byte[] saltPart(byte[] encrypted) {
		return subArray(encrypted, 0, saltGenerator.getKeyLength());
	}

	private byte[] cipherPart(byte[] encrypted, byte[] salt) {
		return subArray(encrypted, salt.length, encrypted.length);
	}
}
