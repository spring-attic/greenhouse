package com.springsource.greenhouse.connect.providers;

import org.springframework.social.linkedin.LinkedInOperations;
import org.springframework.social.linkedin.LinkedInTemplate;

import com.springsource.greenhouse.connect.AbstractServiceProvider;
import com.springsource.greenhouse.connect.AccountConnectionRepository;
import com.springsource.greenhouse.connect.ServiceProviderParameters;
import com.springsource.greenhouse.connect.OAuthToken;

public final class LinkedInServiceProvider extends AbstractServiceProvider<LinkedInOperations> {
	
	public LinkedInServiceProvider(ServiceProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	protected LinkedInOperations createServiceOperations(OAuthToken accessToken) {
		if (accessToken == null) {
			throw new IllegalStateException("Cannot access LinkedIn without an access token");
		}
		return new LinkedInTemplate(getApiKey(), getSecret(), accessToken.getValue(), accessToken.getSecret());
	}

	protected String fetchProviderAccountId(LinkedInOperations linkedIn) {
		return linkedIn.getProfileId();
	}

	protected String buildProviderProfileUrl(String linkedInId, LinkedInOperations linkedIn) {
		return linkedIn.getProfileUrl();
	}
	
}