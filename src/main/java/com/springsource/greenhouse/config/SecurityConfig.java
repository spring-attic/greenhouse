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
package com.springsource.greenhouse.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springsource.greenhouse.account.GreenhousePasswordEncoder;

/**
 * Spring Security Configuration.
 * Applies the policies that secure the Greenhouse web application.
 * <p>
 * In embedded mode, we don't bother with any password encoding or data encryption for developer convenience and ease of developer testing.
 * We also store temporary OAuth sessions in an in-memory ConcurrentHashMap.
 * </p>
 * <p>
 * In standard mode, we apply standard password encoding (SHA-256 1024 iteration hashing + random salting)
 * and encryption (Password-based 256-Bit AES + site-global salt + secure random 16-byte iV handling).
 * We store temporary OAuth sessions in the Redis key-value store.
 * </p>
 * <p>
 * Spring Security is currently best configured using its XML namespace, so this class imports a XML file containing most of the configuration information.
 * The PasswordEncoder, TextEncryptor, and OAuthSessionManager are configured in Java.
 * </p>
 * @author Keith Donald
 */
@Configuration
@ImportResource("classpath:com/springsource/greenhouse/config/security.xml")
public class SecurityConfig {

	/**
	 * Embedded Security configuration (not secure).
	 * @author Keith Donald
	 */
	@Configuration
	@Profile("embedded")
	static class Embedded {

		@Bean
		public PasswordEncoder passwordEncoder() {
			return NoOpPasswordEncoder.getInstance();
		}
		
		@Bean
		public TextEncryptor textEncryptor() {
			return Encryptors.noOpText();
		}
		
	}

	/**
	 * Standard security configuration (secure).
	 * @author Keith Donald
	 */
	@Configuration
	@Profile("standard")
	static class Standard {

		@Inject
		private Environment environment;

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new GreenhousePasswordEncoder(getEncryptPassword());
		}

		@Bean
		public TextEncryptor textEncryptor() {
			return Encryptors.queryableText(getEncryptPassword(), environment.getProperty("security.encryptSalt"));
		}

		// helpers
		
		private String getEncryptPassword() {
			return environment.getProperty("security.encryptPassword");
		}
		
	}
	
}