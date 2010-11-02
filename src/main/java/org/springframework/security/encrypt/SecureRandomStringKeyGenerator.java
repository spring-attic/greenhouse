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

import static org.springframework.security.encrypt.EncodingUtils.hexEncode;

/**
 * StringKeyGenerator that uses SecureRandom to generate randomly-generated hex-encoded String keys.
 * @author Keith Donald
 */
public final class SecureRandomStringKeyGenerator implements StringKeyGenerator {

	private final SecureRandomKeyGenerator keyGenerator;
    
    public SecureRandomStringKeyGenerator() {
    	keyGenerator = new SecureRandomKeyGenerator();
    }
    
    public SecureRandomStringKeyGenerator(String algorithm, String provider, int keyLength) {
    	keyGenerator = new SecureRandomKeyGenerator(algorithm, provider, keyLength);
    }
   
	public String generateKey() {
        return hexEncode(keyGenerator.generateKey());
	}

}
