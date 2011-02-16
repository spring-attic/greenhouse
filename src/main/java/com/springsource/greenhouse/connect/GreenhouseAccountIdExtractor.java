package com.springsource.greenhouse.connect;

import java.io.Serializable;

import org.springframework.social.web.connect.AccountIdExtractor;
import org.springframework.web.context.request.WebRequest;

import com.springsource.greenhouse.account.Account;

public class GreenhouseAccountIdExtractor implements AccountIdExtractor {

	@Override
	public Serializable extractAccountId(WebRequest request) {
		// relies on AccountExposingHandlerInterceptor to have put thte account into the request
		Account account = (Account) request.getAttribute("account", WebRequest.SCOPE_REQUEST);
		return account.getId();
	}

}
