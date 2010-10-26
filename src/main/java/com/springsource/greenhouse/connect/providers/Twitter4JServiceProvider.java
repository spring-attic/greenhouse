package com.springsource.greenhouse.connect.providers;

import java.util.Properties;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.http.AccessToken;
import twitter4j.http.Authorization;
import twitter4j.http.OAuthAuthorization;

import com.springsource.greenhouse.connect.AbstractServiceProvider;
import com.springsource.greenhouse.connect.AccountConnectionRepository;
import com.springsource.greenhouse.connect.ServiceProviderParameters;
import com.springsource.greenhouse.connect.OAuthToken;

public class Twitter4JServiceProvider extends AbstractServiceProvider<Twitter> {
	
	public Twitter4JServiceProvider(ServiceProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	protected Twitter createServiceOperations(OAuthToken accessToken) {
		TwitterFactory twitterFactory = new TwitterFactory();
		AccessToken oauthToken = new AccessToken(accessToken.getValue(), accessToken.getSecret());
		Configuration configuration = new PropertyConfiguration(new Properties());
		Authorization authorization = new OAuthAuthorization(configuration, getApiKey(), getSecret(), oauthToken);
		return accessToken != null ? twitterFactory.getInstance(authorization) : twitterFactory.getInstance();
	}

	protected String fetchProviderAccountId(Twitter twitter) {
		try {
			return twitter.getScreenName();
		} catch (TwitterException e) {
			return null;
		}
	}

	protected String buildProviderProfileUrl(String screenName, Twitter twitter) {
		return "http://www.twitter.com/" + screenName;
	}
}
