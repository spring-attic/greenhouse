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

import java.util.List;

/**
 * Manages member Account information.
 * @author Keith Donald
 */
public interface AccountRepository {

	/**
	 * Create a new Account and add it to this repository.
	 * @param person the person we are creating an Account for
	 * @return the new account
	 * @throws EmailAlreadyOnFileException the person's email address is already on file
	 */
	Account createAccount(Person person) throws EmailAlreadyOnFileException;

	/**
	 * Authenticate a local member account via sign-in and password.
	 * @param signin the signin name; can either be an account username or email address
	 * @param password the member's password of at least 6 characters
	 * @return the authenticated Account, if authentication was successful
	 * @throws SignInNotFoundException an Account could not be found with the provided signin name
	 * @throws InvalidPasswordException the password was not valid
	 */
	Account authenticate(String signin, String password) throws SignInNotFoundException, InvalidPasswordException;

	/**
	 * Change an Account password to the password provided.
	 * Expected to be called internally after the user password change verification process.
	 * @param accountId the internal identifier of the account
	 * @param password the new password
	 */
	void changePassword(Long accountId, String password);
	
	/**
	 * Find an Account by its internal identifier.
	 */
	Account findById(Long accountId);

	/**
	 * Find all Accounts for the given set of IDs.
	 */
	List<ProfileReference> findProfileReferencesByIds(List<Long> accountIds);

	/**
	 * Find an Account by an eligible sign-in name. The sign-in name may be an account username, or an account email address, if that email address is unique.
	 * @throws SignInNotFoundException an Account could not be found with the provided signin name
	 */
	Account findBySignin(String signin) throws SignInNotFoundException;

}