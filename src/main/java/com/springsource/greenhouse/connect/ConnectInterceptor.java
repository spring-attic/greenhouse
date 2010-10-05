package com.springsource.greenhouse.connect;

import org.springframework.social.core.SocialOperations;
import org.springframework.web.context.request.WebRequest;

import com.springsource.greenhouse.account.Account;

public interface ConnectInterceptor {
	boolean supportsProvider(String providerName);

	void preConnect(WebRequest request);

	void postConnect(WebRequest request, SocialOperations api, Account account);
}
