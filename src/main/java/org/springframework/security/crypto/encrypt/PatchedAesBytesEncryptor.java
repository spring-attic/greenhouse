package org.springframework.security.crypto.encrypt;

import static org.springframework.security.crypto.encrypt.CipherUtils.doFinal;
import static org.springframework.security.crypto.encrypt.CipherUtils.initCipher;
import static org.springframework.security.crypto.encrypt.CipherUtils.newCipher;
import static org.springframework.security.crypto.encrypt.CipherUtils.newSecretKey;
import static org.springframework.security.crypto.util.EncodingUtils.concatenate;
import static org.springframework.security.crypto.util.EncodingUtils.subArray;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;

//PATCH for https://jira.springsource.org/browse/SEC-1751

final class PatchedAesBytesEncryptor implements BytesEncryptor {

    private final SecretKey secretKey;

    private final Cipher encryptor;

    private final Cipher decryptor;

    private final BytesKeyGenerator ivGenerator;

    public PatchedAesBytesEncryptor(String password, CharSequence salt) {
    	this(password, salt, null);
    }

    public PatchedAesBytesEncryptor(String password, CharSequence salt, BytesKeyGenerator ivGenerator) {
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), Hex.decode(salt), 1024, 256);
        SecretKey secretKey = newSecretKey("PBKDF2WithHmacSHA1", keySpec);
        this.secretKey = new SecretKeySpec(secretKey.getEncoded(), "AES");
        encryptor = newCipher(AES_ALGORITHM);
        decryptor = newCipher(AES_ALGORITHM);
        this.ivGenerator = ivGenerator != null ? ivGenerator : NULL_IV_GENERATOR;
    }

    public byte[] encrypt(byte[] bytes) {
        synchronized (encryptor) {
            byte[] iv = ivGenerator.generateKey();
            initCipher(encryptor, Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] encrypted = doFinal(encryptor, bytes);
            return concatenate(iv, encrypted);
        }
    }

    public byte[] decrypt(byte[] encryptedBytes) {
        synchronized (decryptor) {
            byte[] iv = ivPart(encryptedBytes);
            initCipher(decryptor, Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            return doFinal(decryptor, cipherPart(encryptedBytes, iv));
        }
    }

    // internal helpers

    private byte[] ivPart(byte[] encrypted) {
        return subArray(encrypted, 0, ivGenerator.getKeyLength());
    }

    private byte[] cipherPart(byte[] encrypted, byte[] iv) {
        return subArray(encrypted, iv.length, encrypted.length);
    }

    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    
    private static final BytesKeyGenerator NULL_IV_GENERATOR = KeyGenerators.shared(16);
    
}