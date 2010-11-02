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

import javax.validation.constraints.Size;

import com.springsource.greenhouse.validation.Confirm;

/**
 * Model for a change password or reset password form.
 * @author Keith Donald
 */
@Confirm(field="password")
public class ChangePasswordForm {
	
	@Size(min=6, message="must be at least 6 characters")
	private String password;

	private String confirmPassword;
		
	/**
	 * The desired new password.
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * A confirmation of the desired password.
	 * Must match {@link ChangePasswordForm#getPassword()}.
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}