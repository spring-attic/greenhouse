package com.springsource.greenhouse.signup;

import org.springframework.integration.annotation.Gateway;

public interface SignupMessageGateway {
	
	@Gateway
	void publish(SignupMessage message);
	
}
