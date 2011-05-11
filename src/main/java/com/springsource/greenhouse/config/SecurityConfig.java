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
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@ImportResource("classpath:com/springsource/greenhouse/config/security.xml")
@Import({Standard.class, Embedded.class})
public class SecurityConfig {

	@Configuration
	@Profile("standard")
	static class Standard {

		@Inject
		private Environment environment;
		
		@Bean
		public PasswordEncoder passwordEncoder() {
			return new StandardPasswordEncoder(getEncryptPassword());
		}

		@Bean
		public TextEncryptor textEncryptor() {
			return Encryptors.text(getEncryptPassword(), environment.getProperty("security.salt"));
		}

		private String getEncryptPassword() {
			return environment.getProperty("security.encryptPassword");
		}
		
	}
	
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
	
}
