package com.springsource.greenhouse.connect.providers;

import org.springframework.social.linkedin.LinkedInOperations;
import org.springframework.social.linkedin.LinkedInTemplate;

import com.springsource.greenhouse.connect.AbstractAccountProvider;
import com.springsource.greenhouse.connect.AccountConnectionRepository;
import com.springsource.greenhouse.connect.AccountProviderParameters;
import com.springsource.greenhouse.connect.OAuthToken;

public final class LinkedInAccountProvider extends AbstractAccountProvider<LinkedInOperations> {
	
	public LinkedInAccountProvider(AccountProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	public LinkedInOperations createApi(OAuthToken accessToken) {
		if (accessToken == null) {
			throw new IllegalStateException("Cannot access LinkedIn without an access token");
		}
		return new LinkedInTemplate(getApiKey(), getSecret(), accessToken.getValue(), accessToken.getSecret());
	}

	public String getProviderAccountId(LinkedInOperations api) {
		return api.getProfileId();
	}

	public String getProviderProfileUrl(LinkedInOperations api) {
		return api.getProfileUrl();
	}
	
	public static LinkedInOperations apiForAccount(LinkedInAccountProvider instance, Long accountId) {
		return instance.getApi(accountId);
	}

}