package com.springsource.greenhouse.connect.providers;

import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.facebook.FacebookTemplate;

import com.springsource.greenhouse.connect.AbstractServiceProvider;
import com.springsource.greenhouse.connect.AccountConnectionRepository;
import com.springsource.greenhouse.connect.ServiceProviderParameters;
import com.springsource.greenhouse.connect.OAuthToken;

public final class FacebookServiceProvider extends AbstractServiceProvider<FacebookOperations> {
	
	public FacebookServiceProvider(ServiceProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	protected FacebookOperations createServiceOperations(OAuthToken accessToken) {
		if (accessToken == null) {
			throw new IllegalStateException("Cannot access Facebook without an access token");
		}
		return new FacebookTemplate(accessToken.getValue());
	}

	protected String fetchProviderAccountId(FacebookOperations facebook) {
		return facebook.getProfileId();
	}

	protected String buildProviderProfileUrl(String facebookId, FacebookOperations facebook) {
		return "http://www.facebook.com/profile.php?id=" + facebookId;
	}

}