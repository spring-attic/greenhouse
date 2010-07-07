package com.springsource.greenhouse.signin.password;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class ResetPasswordRequestForm {
	@NotEmpty
	@Size(max=320)
	private String username;

	public void setUsername(String username) {
	    this.username = username;
    }

	public String getUsername() {
	    return username;
    }	
}
