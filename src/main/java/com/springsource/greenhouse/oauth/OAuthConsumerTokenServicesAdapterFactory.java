package com.springsource.greenhouse.oauth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;
import com.springsource.greenhouse.account.Account;
import org.springframework.social.oauth.AccessTokenServices;


public class OAuthConsumerTokenServicesAdapterFactory implements OAuthConsumerTokenServicesFactory {

	private final AccessTokenServices tokenServices;

	public OAuthConsumerTokenServicesAdapterFactory(AccessTokenServices tokenServices) {
		this.tokenServices = tokenServices;
	}

	public OAuthConsumerTokenServices getTokenServices(Authentication authentication, HttpServletRequest request) {
		Account account = (Account) authentication.getPrincipal();
		return new OAuthConsumerTokenServicesAdapter(request.getSession(true), tokenServices, account);
	}
}
