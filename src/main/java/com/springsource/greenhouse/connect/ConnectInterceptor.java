package com.springsource.greenhouse.connect;

import org.springframework.web.context.request.WebRequest;

import com.springsource.greenhouse.account.Account;

public interface ConnectInterceptor<S> {
	
	void preConnect(ServiceProvider<S> provider, WebRequest request);

	void postConnect(ServiceProvider<S> provider, Account account, WebRequest request);
	
}
