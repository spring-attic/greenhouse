package com.springsource.greenhouse.connect.providers;

import org.springframework.social.twitter.TwitterOperations;
import org.springframework.social.twitter.TwitterTemplate;

import com.springsource.greenhouse.connect.AbstractServiceProvider;
import com.springsource.greenhouse.connect.AccountConnectionRepository;
import com.springsource.greenhouse.connect.ServiceProviderParameters;
import com.springsource.greenhouse.connect.OAuthToken;

public final class TwitterServiceProvider extends AbstractServiceProvider<TwitterOperations> {
	
	public TwitterServiceProvider(ServiceProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	protected TwitterOperations createServiceOperations(OAuthToken accessToken) {
		return accessToken != null ? new TwitterTemplate(getApiKey(), getSecret(), accessToken.getValue(), accessToken.getSecret()) : new TwitterTemplate();
	}

	protected String fetchProviderAccountId(TwitterOperations twitter) {
		return twitter.getProfileId();
	}

	protected String buildProviderProfileUrl(String screenName, TwitterOperations twitter) {
		return "http://www.twitter.com/" + screenName;
	}
	
}