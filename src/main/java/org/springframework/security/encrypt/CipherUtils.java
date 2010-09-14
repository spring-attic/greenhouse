package org.springframework.security.encrypt;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class CipherUtils {
	
	private CipherUtils() {		
	}
	
	public static SecretKey newSecretKey(String algorithm, String secret) {
		try {
			PBEKeySpec pbeKeySpec = new PBEKeySpec(secret.toCharArray());
			SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
			return factory.generateSecret(pbeKeySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("Not a valid encryption algorithm", e);
		} catch (InvalidKeySpecException e) {
			throw new IllegalArgumentException("Not a valid secert key", e);
		}
	}
		
	public static Cipher newCipher(String algorithm) {
		try {
			return Cipher.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("Not a valid encryption algorithm", e);
		} catch (NoSuchPaddingException e) {
			throw new IllegalStateException("Should not happen", e);
		}
	}
	
	public static void initCipher(Cipher cipher, int mode, SecretKey secretKey, byte[] salt, int iterationCount) {
		try {
			cipher.init(mode, secretKey, new PBEParameterSpec(salt, iterationCount));
		} catch (InvalidKeyException e) {
			throw new IllegalArgumentException("Unable to initialize due to invalid secret key", e);			
		} catch (InvalidAlgorithmParameterException e) {
			throw new IllegalStateException("Unable to initialize due to invalid decryption parameter spec", e);
		}
	}
	
	public static byte[] doFinal(Cipher cipher, byte[] input) {
		try {
			return cipher.doFinal(input);
		} catch (IllegalBlockSizeException e) {
			throw new IllegalStateException("Unable to encrypt due to illegal block size", e);
		} catch (BadPaddingException e) {
			throw new IllegalStateException("Unable to encrypt due to bad padding", e);
		}		
	}
}
