package com.springsource.greenhouse.signup.mail;

import org.springframework.integration.annotation.Gateway;

public interface SignupMessageGateway {
	@Gateway
	void publish(SignupMessage message);
}
