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
package com.springsource.greenhouse.signup;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.AccountUtils;
import com.springsource.greenhouse.account.EmailAlreadyOnFileException;
import com.springsource.greenhouse.account.Person;

/**
 * @author Keith Donald
 */
public class SignupHelper {

	private final AccountRepository accountRepository;
	
	private final SignedUpGateway gateway;
	
	public SignupHelper(AccountRepository accountRepository, SignedUpGateway gateway) {
		this.accountRepository = accountRepository;
		this.gateway = gateway;
	}

	public boolean signup(SignupForm form, BindingResult formBinding) {
		return signup(form, formBinding, null);
	}
	
	public boolean signup(SignupForm form, BindingResult formBinding, SignupCallback callback) {
		try {
			Account account = createAccount(form.createPerson(),callback);
			gateway.signedUp(account);
			AccountUtils.signin(account);
			return true;
		} catch (EmailAlreadyOnFileException e) {
			formBinding.rejectValue("email", "account.duplicateEmail", "already on file");
			return false;
		}		
	}
	
	@Transactional
	private Account createAccount(Person person, SignupCallback callback) throws EmailAlreadyOnFileException {
		Account account = accountRepository.createAccount(person);
		if (callback != null) {
			callback.postCreateAccount(account);
		}
		return account;
	}
	
	public interface SignupCallback {
		void postCreateAccount(Account account);
	}

}