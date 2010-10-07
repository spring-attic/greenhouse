package com.springsource.greenhouse.config;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.linkedin.LinkedInOperations;
import org.springframework.social.tripit.TripItOperations;
import org.springframework.social.twitter.TwitterOperations;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.connect.AccountProvider;

public class AccountProvidersApiConfiguration {

	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public TwitterOperations twitterApi(AccountProvider<TwitterOperations> twitterAccountProvider, @Value("request.getAttribute('account')") Account account) {
		return twitterAccountProvider.getApi(accountId(account));
	}

	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public FacebookOperations facebookApi(AccountProvider<FacebookOperations> facebookAccountProvider, @Value("request.getAttribute('account')") Account account) {
		return facebookAccountProvider.getApi(accountId(account));
	}

	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public LinkedInOperations apiForAccount(AccountProvider<LinkedInOperations> linkedInAccountProvider, @Value("request.getAttribute('account')") Account account) {
		return linkedInAccountProvider.getApi(accountId(account));
	}

	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public TripItOperations tripItApi(AccountProvider<TripItOperations> tripItAccountProvider, @Value("request.getAttribute('account')") Account account) {
		return tripItAccountProvider.getApi(accountId(account));
	}

	private static Long accountId(Account account) {
		return account != null ? account.getId() : null;
	}
	
}