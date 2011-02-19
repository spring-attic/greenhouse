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
