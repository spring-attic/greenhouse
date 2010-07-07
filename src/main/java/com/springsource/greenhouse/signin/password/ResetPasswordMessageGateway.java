package com.springsource.greenhouse.signin.password;

import org.springframework.integration.annotation.Gateway;

public interface ResetPasswordMessageGateway {
	@Gateway
	void publish(ResetPasswordMessage message);
}
