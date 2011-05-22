package org.springframework.security.crypto.encrypt;

import java.io.UnsupportedEncodingException;

import org.springframework.security.crypto.codec.Hex;

// PATCH for https://jira.springsource.org/browse/SEC-1752
	
/**
 * Delegates to an {@link BytesEncryptor} to encrypt text strings.
 * Raw text strings are UTF-8 encoded before being passed to the encryptor.
 * Encrypted strings are returned hex-encoded.
 * @author Keith Donald
 */
public class PatchedHexEncodingTextEncryptor implements TextEncryptor {

	private final BytesEncryptor encryptor;

	public PatchedHexEncodingTextEncryptor(BytesEncryptor encryptor) {
		this.encryptor = encryptor;
	}

	public String encrypt(String text) {
		return new String(Hex.encode(encryptor.encrypt(Utf8.encode(text))));
	}

	public String decrypt(String encryptedText) {
		return Utf8.decode(encryptor.decrypt(Hex.decode(encryptedText)));
	}
	
	public static class Utf8 {
		public static byte[] encode(String text) {
			try {
				return text.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new IllegalStateException("Should not happen");
			}
		}

		public static String decode(byte[] decrypt) {
			try {
				return new String(decrypt, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new IllegalStateException("Should not happen");
			}
		}
	}
}

