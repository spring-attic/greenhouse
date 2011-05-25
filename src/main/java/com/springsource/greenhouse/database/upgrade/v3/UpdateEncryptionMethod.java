/*
 * Copyright 2010-2011 the original author or authors.
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
package com.springsource.greenhouse.database.upgrade.v3;

import static com.springsource.greenhouse.database.upgrade.v3.CipherUtils.doFinal;
import static com.springsource.greenhouse.database.upgrade.v3.CipherUtils.initCipher;
import static com.springsource.greenhouse.database.upgrade.v3.CipherUtils.newCipher;
import static com.springsource.greenhouse.database.upgrade.v3.CipherUtils.newSecretKey;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.versioned.AbstractDatabaseChange;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * Changes the data encryption method used from DES to AES.
 * @author Keith Donald
 */
public class UpdateEncryptionMethod extends AbstractDatabaseChange {
	
	private final StringEncryptor oldEncryptor;
	
	private final TextEncryptor newEncryptor;

	@Inject
	public UpdateEncryptionMethod(Environment environment, TextEncryptor newEncryptor) {
		this.oldEncryptor = new SearchableStringEncryptor(environment.getProperty("security.encryptPassword"), environment.getProperty("security.encryptSalt"));
		this.newEncryptor = newEncryptor;
	}
	
	@Override
	protected Statement doCreateStatement(Connection connection) throws SQLException {
		return connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
	}

	@Override
	protected void doExecuteStatement(Statement statement) throws SQLException {
		ResultSet rs = statement.executeQuery("select apiKey, secret from App");
		while (rs.next()) {
			rs.updateString("apiKey", encrypt(decrypt(rs.getString("apiKey"))));
			rs.updateString("secret", encrypt(decrypt(rs.getString("secret"))));
			rs.updateRow();
		}
		rs = statement.executeQuery("select accessToken, secret from AppConnection");
		while (rs.next()) {
			rs.updateString("accessToken", encrypt(decrypt(rs.getString("accessToken"))));
			rs.updateString("secret", encrypt(decrypt(rs.getString("secret"))));
			rs.updateRow();
		}
		rs = statement.executeQuery("select member, provider, accessToken, secret from AccountConnection");
		while (rs.next()) {
			rs.updateString("accessToken", encrypt(decrypt(rs.getString("accessToken"))));
			String secret = rs.getString("secret");
			if (secret != null) {
				rs.updateString("secret", encrypt(decrypt(secret)));
			}
			rs.updateRow();			
		}		
	}

	private String encrypt(String decrypted) {
		return newEncryptor.encrypt(decrypted);
	}
	
	private String decrypt(String encrypted) {
		return oldEncryptor.decrypt(encrypted);
	}
	
	private static class SearchableStringEncryptor implements StringEncryptor {

		private final Cipher encryptor;

		private final Cipher decryptor;

		public SearchableStringEncryptor(String password, String salt) {
			String algorithm = "PBEWithMD5AndDES";
			byte[] saltBytes = Hex.decode(salt);
			SecretKey secretKey = newSecretKey(algorithm, password);		
			encryptor = newCipher(algorithm);
			initCipher(encryptor, Cipher.ENCRYPT_MODE, secretKey, saltBytes, 1000);
			decryptor = newCipher(algorithm);
			initCipher(decryptor, Cipher.DECRYPT_MODE, secretKey, saltBytes, 1000);
		}

		public String encrypt(String text) {
			return new String(Hex.encode(doFinal(encryptor, Utf8.encode(text))));
		}

		public String decrypt(String encryptedText) {
			return Utf8.decode(doFinal(decryptor, Hex.decode(encryptedText)));
		}

	}
	
	interface StringEncryptor {

		String encrypt(String string);

		String decrypt(String encrypted);

	}
}
