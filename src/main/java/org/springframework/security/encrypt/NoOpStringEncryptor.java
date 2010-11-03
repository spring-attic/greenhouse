/*
 * Copyright 2010 the original author or authors.
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
package org.springframework.security.encrypt;

/**
 * A String Encryptor that does nothing. Useful for testing.
 * @author Keith Donald
 */
public class NoOpStringEncryptor implements StringEncryptor {

	public String encrypt(String string) {
		return string;
	}

	public String decrypt(String encrypted) {
		return encrypted;
	}

	/**
	 * Get the singleton {@link NoOpStringEncryptor}.
	 */
	public static StringEncryptor getInstance() {
		return INSTANCE;
	}
	
	private static final StringEncryptor INSTANCE = new NoOpStringEncryptor();
	
	private NoOpStringEncryptor() {
		
	}
}
