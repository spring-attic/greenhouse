package com.springsource.greenhouse.reset;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class ChangePasswordForm {
	
	@NotEmpty
	@Size(min=6)
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
