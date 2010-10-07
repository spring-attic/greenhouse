package com.springsource.greenhouse.connect.providers;

import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.facebook.FacebookTemplate;

import com.springsource.greenhouse.connect.AbstractAccountProvider;
import com.springsource.greenhouse.connect.AccountConnectionRepository;
import com.springsource.greenhouse.connect.AccountProviderParameters;
import com.springsource.greenhouse.connect.OAuthToken;

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
	
	public static FacebookOperations apiForAccount(FacebookAccountProvider instance, Long accountId) {
		return instance.getApi(accountId);
	}

}