package com.springsource.greenhouse.connect;

import org.springframework.web.context.request.WebRequest;

import com.springsource.greenhouse.account.Account;

public interface ConnectInterceptor<T> {
	
	void preConnect(AccountProvider<T> provider, WebRequest request);

	void postConnect(AccountProvider<T> provider, Account account, WebRequest request);
	
}
