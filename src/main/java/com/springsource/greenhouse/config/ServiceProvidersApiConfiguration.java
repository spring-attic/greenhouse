package com.springsource.greenhouse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.linkedin.LinkedInOperations;
import org.springframework.social.tripit.TripItOperations;
import org.springframework.social.twitter.TwitterOperations;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.connect.ServiceProvider;

public class ServiceProvidersApiConfiguration {

	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public TwitterOperations twitter(ServiceProvider<TwitterOperations> twitterProvider, @Value("#{request.getAttribute('account')}") Account account) {
		return twitterProvider.getServiceOperations(accountId(account));
	}

	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public FacebookOperations facebook(ServiceProvider<FacebookOperations> facebookProvider, @Value("#{request.getAttribute('account')}") Account account) {
		return facebookProvider.getServiceOperations(accountId(account));
	}

	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public LinkedInOperations linkedIn(ServiceProvider<LinkedInOperations> linkedInProvider, @Value("#{request.getAttribute('account')}") Account account) {
		return linkedInProvider.getServiceOperations(accountId(account));
	}

	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public TripItOperations tripIt(ServiceProvider<TripItOperations> tripItProvider, @Value("#{request.getAttribute('account')}") Account account) {
		return tripItProvider.getServiceOperations(accountId(account));
	}

	private static Long accountId(Account account) {
		return account != null ? account.getId() : null;
	}
	
}