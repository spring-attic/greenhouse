package com.springsource.greenhouse.reset;

import javax.validation.constraints.Size;

import com.springsource.greenhouse.validation.Confirm;

@Confirm(field="password")
public class ChangePasswordForm {
	
	@Size(min=6, message="must be at least 6 characters")
	private String password;

	private String confirmPassword;
		
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
