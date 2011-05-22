package org.springframework.security.crypto.encrypt;

//PATCH for https://jira.springsource.org/browse/SEC-1751

/**
 * Factory for commonly used encryptors.
 * Defines the public API for constructing {@link BytesEncryptor} and {@link TextEncryptor} implementations.
 *
 * @author Keith Donald
 */
public class PatchedEncryptors {

    public static TextEncryptor queryableText(CharSequence password, CharSequence salt) {
        return new PatchedHexEncodingTextEncryptor(new PatchedAesBytesEncryptor(password.toString(), salt));
    }
}
