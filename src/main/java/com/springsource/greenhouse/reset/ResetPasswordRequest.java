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
package com.springsource.greenhouse.reset;

import com.springsource.greenhouse.account.Account;

/**
 * A request made by a member, or someone on behalf of the member, to reset the member's password.
 * @author Keith Donald
 */
public class ResetPasswordRequest {
	
	private String token;
	
	private Account account;

	public ResetPasswordRequest(String token, Account account) {
		this.token = token;
		this.account = account;
	}

	/**
	 * The token that identifies this reset password request.
	 */
	public String getToken() {
		return token;
	}

	/**
	 * The member Account for which this reset password request was made.
	 */
	public Account getAccount() {
		return account;
	}
		
}
