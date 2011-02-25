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
package com.springsource.greenhouse.signin;

import javax.inject.Inject;

import org.springframework.social.facebook.web.FacebookSigninController;
import org.springframework.social.web.signin.SignInService;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.AccountUtils;

/**
 * Used to support the user signing in with their Facebook account.
 * @author Keith Donald
 * @see FacebookSigninController
 */
public class AccountSignInService implements SignInService<Long> {

	private final AccountRepository accountRepository;

	@Inject
	public AccountSignInService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public void signIn(Long accountId) {
		Account account = accountRepository.findById(accountId);
		AccountUtils.signin(account);
	}

}
