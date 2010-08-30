package com.springsource.greenhouse.signup;

import org.springframework.integration.annotation.Gateway;
import org.springframework.social.account.Account;


public interface SignedUpGateway {
	
	@Gateway
	void signedUp(Account account);
	
}
