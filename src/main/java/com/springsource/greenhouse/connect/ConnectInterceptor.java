package com.springsource.greenhouse.connect;

import org.springframework.social.core.SocialProviderOperations;
import org.springframework.web.context.request.WebRequest;

import com.springsource.greenhouse.account.Account;

public interface ConnectInterceptor {
	boolean supportsProvider(String providerName);

	void preConnect(WebRequest request);

	void postConnect(WebRequest request, SocialProviderOperations api, Account account);
}
