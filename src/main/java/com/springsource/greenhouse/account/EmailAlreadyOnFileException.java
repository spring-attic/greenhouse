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
package com.springsource.greenhouse.account;

/**
 * Sign-up failed because the email address the Person provided is already on file.
 * The same email address can not be shared by different people.
 * @author Keith Donald
 */
@SuppressWarnings("serial")
public class EmailAlreadyOnFileException extends AccountException {
	
	private String email;
	
	public EmailAlreadyOnFileException(String email) {
		super("email already on file");
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
}
