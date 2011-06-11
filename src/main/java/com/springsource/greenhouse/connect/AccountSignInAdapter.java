/*
 * Copyright 2011 the original author or authors.
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
package com.springsource.greenhouse.connect;

import javax.inject.Inject;

import org.springframework.social.connect.signin.web.SignInAdapter;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.AccountUtils;

/**
 * Used to support the user signing in with their Facebook account.
 * @author Keith Donald
 * @see FacebookSigninController
 */
public class AccountSignInAdapter implements SignInAdapter {

	private final AccountRepository accountRepository;

	@Inject
	public AccountSignInAdapter(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public void signIn(String userId) {
		Account account = accountRepository.findById(Long.valueOf(userId));
		AccountUtils.signin(account);
	}

}
