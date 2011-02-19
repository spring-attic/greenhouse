package com.springsource.greenhouse.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@Profile("standard")
public class StandardEncryptionConfig {

	@Bean
	public PasswordEncoder passwordEncoder(@Value("#{environment['security.encryptPassword']}") String secret) {
		return new StandardPasswordEncoder(secret);
	}
	
	@Bean
	public TextEncryptor textEncryptor(@Value("#{environment['security.encryptPassword']}") String password,
			@Value("#{environment['security.encryptSalt']}") String salt) {
		return Encryptors.queryableText(password, salt);
	}
	
}
