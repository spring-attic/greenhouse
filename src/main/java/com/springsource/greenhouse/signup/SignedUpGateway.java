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

import org.springframework.integration.annotation.Gateway;
import com.springsource.greenhouse.account.Account;

/**
 * Spring Integration Message Gateway for sending member signed up Messages.
 * @author Keith Donald
 */
public interface SignedUpGateway {

	/**
	 * Called by the SignupController after signing up a new user to broadcast a signup notification.
	 * Subscribers attached to the signup channel can then process the notification.
	 * Decouples the message sender from the messaging infrastructure and message handling pipeline.
	 * Generally called after the new member Account has been saved in the system of record. 
	 */
	@Gateway
	void signedUp(Account account);
	
}
