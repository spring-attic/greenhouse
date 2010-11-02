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
import static org.springframework.security.encrypt.EncodingUtils.hexDecode;
import static org.springframework.security.encrypt.EncodingUtils.hexEncode;
import static org.springframework.security.encrypt.EncodingUtils.utf8Decode;
import static org.springframework.security.encrypt.EncodingUtils.utf8Encode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * @author Keith Donald
 */
//TODO evaluate AES for higher-level of security
public class SearchableStringEncryptor implements StringEncryptor {

	private final Cipher encryptor;

	private final Cipher decryptor;

	public SearchableStringEncryptor(String password, String salt) {
		String algorithm = "PBEWithMD5AndDES";
		byte[] saltBytes = hexDecode(salt);
		SecretKey secretKey = newSecretKey(algorithm, password);		
		encryptor = newCipher(algorithm);
		initCipher(encryptor, Cipher.ENCRYPT_MODE, secretKey, saltBytes, 1000);
		decryptor = newCipher(algorithm);
		initCipher(decryptor, Cipher.DECRYPT_MODE, secretKey, saltBytes, 1000);
	}
	
	public String encrypt(String text) {
		return hexEncode(doFinal(encryptor, utf8Encode(text)));
	}

	public String decrypt(String encryptedText) {
		return utf8Decode(doFinal(decryptor, hexDecode(encryptedText)));
	}
	
}
