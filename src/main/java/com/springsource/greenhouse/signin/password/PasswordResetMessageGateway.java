package com.springsource.greenhouse.signin.password;

import org.springframework.integration.annotation.Gateway;

public interface PasswordResetMessageGateway {
	@Gateway
	void publish(PasswordResetMessage message);
}
