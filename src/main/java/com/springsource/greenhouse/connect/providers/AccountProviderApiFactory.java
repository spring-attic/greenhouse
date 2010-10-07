package com.springsource.greenhouse.connect.providers;

import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.linkedin.LinkedInOperations;
import org.springframework.social.tripit.TripItOperations;
import org.springframework.social.twitter.TwitterOperations;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.connect.AccountProvider;

// TODO - this can go away once Spring can autowire using parameterized types as qualifiers
public class AccountProviderApiFactory {
	
	public static TwitterOperations getTwitterApi(AccountProvider<TwitterOperations> twitterProvider, Account account) {
		return twitterProvider.getApi(account != null ? account.getId() : null);
	}

	public static LinkedInOperations getLinkedInApi(AccountProvider<LinkedInOperations> linkedInProvider,
			Account account) {
		return linkedInProvider.getApi(account != null ? account.getId() : null);
	}

	public static TripItOperations getTripItApi(AccountProvider<TripItOperations> tripItProvider, Account account) {
		return tripItProvider.getApi(account != null ? account.getId() : null);
	}

	public static FacebookOperations getFacebookApi(AccountProvider<FacebookOperations> facebookProvider, Account account) {
		return facebookProvider.getApi(account != null ? account.getId() : null);
	}

	private AccountProviderApiFactory() {}
	
}
