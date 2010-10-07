package com.springsource.greenhouse.connect.providers;

import org.springframework.social.twitter.TwitterOperations;
import org.springframework.social.twitter.TwitterTemplate;

import com.springsource.greenhouse.connect.AbstractAccountProvider;
import com.springsource.greenhouse.connect.AccountConnectionRepository;
import com.springsource.greenhouse.connect.AccountProviderParameters;
import com.springsource.greenhouse.connect.OAuthToken;

public final class TwitterAccountProvider extends AbstractAccountProvider<TwitterOperations> {
	
	public TwitterAccountProvider(AccountProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	public TwitterOperations createApi(OAuthToken accessToken) {
		return accessToken != null ? new TwitterTemplate(getApiKey(), getSecret(), accessToken.getValue(), accessToken.getSecret()) : new TwitterTemplate();
	}

	public String getProviderAccountId(TwitterOperations api) {
		return api.getProfileId();
	}

	public String getProviderProfileUrl(TwitterOperations api) {
		return "http://www.twitter.com/" + api.getProfileId();
	}
	
	public static TwitterOperations apiForAccount(TwitterAccountProvider instance, Long accountId) {
		return instance.getApi(accountId);
	}
	
}