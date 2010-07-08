package com.springsource.greenhouse.reset;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class ResetPasswordForm {
	@NotEmpty
	@Size(min=6)
	private String password = "";

	@NotEmpty
	@Size(min=6)
	private String confirmPassword = "";
	
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
	
	@AssertTrue(message="could not confirm")
	public boolean isPasswordConfirmed() {
		return password.equals(confirmPassword);
	}
}
