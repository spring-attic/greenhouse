package com.springsource.greenhouse.connect;

import org.springframework.social.twitter.TwitterOperations;
import org.springframework.social.twitter.TwitterTemplate;

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
}