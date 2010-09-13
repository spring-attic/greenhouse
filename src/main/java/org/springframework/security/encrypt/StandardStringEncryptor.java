package org.springframework.security.encrypt;

import static org.springframework.security.encrypt.EncodingUtils.concatenate;
import static org.springframework.security.encrypt.EncodingUtils.hexDecode;
import static org.springframework.security.encrypt.EncodingUtils.hexEncode;
import static org.springframework.security.encrypt.EncodingUtils.subArray;
import static org.springframework.security.encrypt.EncodingUtils.utf8Decode;
import static org.springframework.security.encrypt.EncodingUtils.utf8Encode;

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

// TODO switch from DES to AES
public class StandardStringEncryptor implements StringEncryptor {

	private SecretKey secretKey;

	private Cipher encryptor;

	private Cipher decryptor;

	private SaltFactory saltFactory;
	
	public StandardStringEncryptor(String secret) {
		init("PBEWithMD5AndDES", secret, null);
	}
	
	public StandardStringEncryptor(String secret, String salt) {
		init("PBEWithMD5AndDES", secret, salt);
	}
	
	public String encrypt(String text) {
		try {
			byte[] salt = saltFactory.getSalt();
			PBEParameterSpec spec = new PBEParameterSpec(salt, 1000);
			byte[] encrypted;
			synchronized (encryptor) {
				encryptor.init(Cipher.ENCRYPT_MODE, secretKey, spec);
				encrypted = encryptor.doFinal(utf8Encode(text));
			}
			if (saltFactory.isRandomSalt()) {
				encrypted = concatenate(salt, encrypted);
			}
			return encode(encrypted);
		} catch (InvalidKeyException e) {
			throw new IllegalArgumentException("Unable to initialize due to invalid secret key", e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new IllegalStateException("Unable to initialize due to invalid encryption parameter spec", e);
		} catch (IllegalBlockSizeException e) {
			throw new IllegalStateException("Unable to encrypt due to illegal block size", e);
		} catch (BadPaddingException e) {
			throw new IllegalStateException("Unable to encrypt due to bad padding", e);
		}
	}

	public String decrypt(String encryptedText) {
		byte[] encrypted = decode(encryptedText);
		byte[] salt = decodeSalt(encrypted);
		if (saltFactory.isRandomSalt()) {
			encrypted = stripSalt(encrypted, salt);
		}
		try {
			PBEParameterSpec spec = new PBEParameterSpec(salt, 1000);
			byte[] decrypted;
			synchronized (decryptor) {
				decryptor.init(Cipher.DECRYPT_MODE, secretKey, spec);
				decrypted = decryptor.doFinal(encrypted);
			}
			return utf8Decode(decrypted);
		} catch (InvalidKeyException e) {
			throw new IllegalArgumentException("Unable to initialize due to invalid secret key", e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new IllegalStateException("Unable to initialize due to invalid decryption parameter spec", e);
		} catch (IllegalBlockSizeException e) {
			throw new IllegalStateException("Unable to decrypt due to illegal block size", e);
		} catch (BadPaddingException e) {
			throw new IllegalStateException("Unable to decrypt due to bad padding", e);
		}
	}

	private void init(String algorithm, String secret, String salt) {
		secretKey = newSecretKey(algorithm, secret);
		encryptor = newCipher(algorithm);
		decryptor = newCipher(algorithm);
		if (salt != null) {
			saltFactory = new SiteGlobalSaltFactory(salt);
		} else {
			saltFactory = new SecureRandomSaltFactory(getSaltLengthFor(encryptor));
		}
	}

	private Cipher newCipher(String algorithm) {
		try {
			return Cipher.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("Not a valid encryption algorithm", e);
		} catch (NoSuchPaddingException e) {
			throw new IllegalStateException("Should not happen", e);
		}
	}

	private SecretKey newSecretKey(String algorithm, String secret) {
		try {
			PBEKeySpec pbeKeySpec = new PBEKeySpec(secret.toCharArray());
			SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
			return factory.generateSecret(pbeKeySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(
					"Not a valid encryption algorithm", e);
		} catch (InvalidKeySpecException e) {
			throw new IllegalArgumentException("Not a valid secert key", e);
		}
	}
	
	private int getSaltLengthFor(Cipher encryptor) {
		int algorithmBlockSize = encryptor.getBlockSize();
		return algorithmBlockSize > 0 ? algorithmBlockSize : 8;
	}
	
	private String encode(byte[] bytes) {
		return hexEncode(bytes);
	}
	
	private byte[] decode(String encoded) {
		return hexDecode(encoded);
	}
	
	private byte[] decodeSalt(byte[] encrypted) {
		if (saltFactory.isRandomSalt()) {
			return subArray(encrypted, 0, saltFactory.getSaltLength());
		} else {
			return saltFactory.getSalt();
		}
	}

	private byte[] stripSalt(byte[] encrypted, byte[] salt) {
		return subArray(encrypted, salt.length, encrypted.length);
	}
}
