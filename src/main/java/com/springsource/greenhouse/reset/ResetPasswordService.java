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

import com.springsource.greenhouse.account.SignInNotFoundException;

/**
 * Service interface for resetting your password.
 * @author Keith Donald
 */
public interface ResetPasswordService {

	/**
	 * Send a reset password mail to the member with the signin name.
	 * The mail contains a link the member may activate to reset their password.
	 * The link includes a token query parameter required to complete the reset password request.
	 * This token may expire after a configurable time period if it goes unused.
	 * @param signin the member's sign name, which may be their public username or their email address
	 * @throws SignInNotFoundException the submitted signin name did not map to a member account
	 */
	void sendResetMail(String signin) throws SignInNotFoundException;

	/**
	 * True if the reset password token is valid.
	 * When false, used to present a suitable error message to the person indicating their reset token is no longer valid and they should request a new one.
	 * Will return false if the token has expired or has already been used.
	 */
	boolean isValidResetToken(String token);

	/**
	 * Reset the user's password to the specified password.
	 * @param token the reset request token required to perform the change password operation
	 * @param password the new password
	 * @throws InvalidResetTokenException the token submitted by the person is invalid
	 */
	void changePassword(String token, String password) throws InvalidResetTokenException;
	
}
