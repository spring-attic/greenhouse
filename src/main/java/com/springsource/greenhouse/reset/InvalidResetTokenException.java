/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.reset;

/**
 * The reset password request token presented is not valid.
 * It may have expired or been already used.
 * @author Keith Donald
 */
@SuppressWarnings("serial")
public class InvalidResetTokenException extends Exception {

	private final String token;
	
	public InvalidResetTokenException(String token) {
		this.token = token;
	}
	
	/**
	 * The invalid request token value.
	 */
	public String getToken() {
		return token;
	}

}
