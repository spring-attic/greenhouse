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
import com.springsource.greenhouse.invite.InviteController;

/**
 * A helper for signing up new users.
 * Encapsulates common signup UI control and business logic.
 * Factored out since signup can be triggered by a normal {@link SignupController} flow or via a {@link InviteController accept invite} flow.
 * @author Keith Donald
 */
public class SignupHelper {

	private final AccountRepository accountRepository;
	
	private final SignedUpGateway gateway;
	
	public SignupHelper(AccountRepository accountRepository, SignedUpGateway gateway) {
		this.accountRepository = accountRepository;
		this.gateway = gateway;
	}

	/**
	 * Signup the person who completed the signup form.
	 * Returns true if signup was successful, false if there was an error.
	 * Records any errors in the form's BindingResult context for display to the person.
	 */
	public boolean signup(SignupForm form, BindingResult formBinding) {
		return signup(form, formBinding, null);
	}
	
	/**
	 * Signup the person who completed the signup form.
	 * Returns true if signup was successful, false if there was an error.
	 * Records any errors in the form's BindingResult context for display to the person.
	 * An optional SignupCallback may be specified for performing custom processing after successful member signup.
	 */
	public boolean signup(SignupForm form, BindingResult formBinding, SignupCallback callback) {
		try {
			Account account = createAccount(form.createPerson(),callback);
			gateway.signedUp(account);
			AccountUtils.signin(account);
			if (callback != null) {
				callback.postSignup(account);
			}
			return true;
		} catch (EmailAlreadyOnFileException e) {
			formBinding.rejectValue("email", "account.duplicateEmail", "already on file");
			return false;
		}		
	}
	
	// internal helpers
	
	@Transactional
	private Account createAccount(Person person, SignupCallback callback) throws EmailAlreadyOnFileException {
		return accountRepository.createAccount(person);
	}
	
	/**
	 * Called after new member Account signup to allow for custom post processing.
	 */
	public interface SignupCallback {
		
		void postSignup(Account account);
		
	}

}