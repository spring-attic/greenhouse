package com.springsource.greenhouse.connect;

import org.springframework.social.tripit.TripItOperations;
import org.springframework.social.tripit.TripItTemplate;

public final class TripItAccountProvider extends AbstractAccountProvider<TripItOperations> {
	
	public TripItAccountProvider(AccountProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	public TripItOperations createApi(OAuthToken accessToken) {
		if (accessToken == null) {
			throw new IllegalStateException("Cannot access TripIt without an access token");
		}
		return new TripItTemplate(getApiKey(), getSecret(), accessToken.getValue(), accessToken.getSecret());
	}

	public String getProviderAccountId(TripItOperations api) {
		return api.getProfileId();
	}

	public String getProviderProfileUrl(TripItOperations api) {
		return api.getProfileUrl();
	}
}