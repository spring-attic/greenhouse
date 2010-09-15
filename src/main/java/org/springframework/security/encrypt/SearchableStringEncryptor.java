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
