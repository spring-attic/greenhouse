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

import com.springsource.greenhouse.connect.AbstractAccountProvider;
import com.springsource.greenhouse.connect.AccountConnectionRepository;
import com.springsource.greenhouse.connect.AccountProviderParameters;
import com.springsource.greenhouse.connect.OAuthToken;

public class Twitter4JAccountProvider extends AbstractAccountProvider<Twitter> {
	
	public Twitter4JAccountProvider(AccountProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	protected Twitter createApi(OAuthToken accessToken) {
		TwitterFactory twitterFactory = new TwitterFactory();
		AccessToken oauthToken = new AccessToken(accessToken.getValue(), accessToken.getSecret());
		Configuration configuration = new PropertyConfiguration(new Properties());
		Authorization authorization = new OAuthAuthorization(configuration, getApiKey(), getSecret(), oauthToken);
		return accessToken != null ? twitterFactory.getInstance(authorization) : twitterFactory.getInstance();
	}

	protected String getProviderAccountId(Twitter api) {
		try {
			return api.getScreenName();
		} catch (TwitterException e) {
			return null;
		}
	}

	protected String getProviderProfileUrl(Twitter api) {
		try {
			return "http://www.twitter.com/" + api.getScreenName();
		} catch (TwitterException e) {
			return null;
		}
	}
}
