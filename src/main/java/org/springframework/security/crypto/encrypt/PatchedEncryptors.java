package org.springframework.security.crypto.encrypt;

import org.springframework.security.crypto.keygen.KeyGenerators;

//PATCH for https://jira.springsource.org/browse/SEC-1751

/**
 * Factory for commonly used encryptors.
 * Defines the public API for constructing {@link BytesEncryptor} and {@link TextEncryptor} implementations.
 *
 * @author Keith Donald
 */
public class PatchedEncryptors {

    public static BytesEncryptor standard(CharSequence password, CharSequence salt) {
        return new AesBytesEncryptor(password.toString(), salt, KeyGenerators.secureRandom(16));
    }
    
    public static TextEncryptor text(CharSequence password, CharSequence salt) {
        return new PatchedHexEncodingTextEncryptor(standard(password, salt));
    }
    
    public static TextEncryptor queryableText(CharSequence password, CharSequence salt) {
        return new PatchedHexEncodingTextEncryptor(new PatchedAesBytesEncryptor(password.toString(), salt));
    }
}
