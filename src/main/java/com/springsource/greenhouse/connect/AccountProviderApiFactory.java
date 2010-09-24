package com.springsource.greenhouse.connect;

import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.twitter.TwitterOperations;

import com.springsource.greenhouse.account.Account;

// TODO - this can go away once Spring can autowire using parameterized types as qualifiers
public class AccountProviderApiFactory {
	
	public static TwitterOperations getTwitterApi(AccountProvider<TwitterOperations> twitterProvider, Account account) {
		return twitterProvider.getApi(account != null ? account.getId() : null);
	}

	public static FacebookOperations getFacebookApi(AccountProvider<FacebookOperations> facebookProvider, Account account) {
		return facebookProvider.getApi(account != null ? account.getId() : null);
	}

	private AccountProviderApiFactory() {}
	
}
