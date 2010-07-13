package com.springsource.greenhouse.signup;

import org.springframework.integration.annotation.Gateway;

import com.springsource.greenhouse.account.Account;

public interface SignedUpMessageGateway {
	
	@Gateway
	void signedUp(Account account);
	
}
