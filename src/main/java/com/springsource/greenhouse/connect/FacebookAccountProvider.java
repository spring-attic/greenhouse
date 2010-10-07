package com.springsource.greenhouse.connect;

import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.facebook.FacebookTemplate;

public final class FacebookAccountProvider extends AbstractAccountProvider<FacebookOperations> {
	
	public FacebookAccountProvider(AccountProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	public FacebookOperations createApi(OAuthToken accessToken) {
		if (accessToken == null) {
			throw new IllegalStateException("Cannot access Facebook without an access token");
		}
		return new FacebookTemplate(accessToken.getValue());
	}

	public String getProviderAccountId(FacebookOperations api) {
		return api.getProfileId();
	}

	public String getProviderProfileUrl(FacebookOperations api) {
		return "http://www.facebook.com/profile.php?id=" + api.getProfileId();
	}
}