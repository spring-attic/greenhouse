package com.springsource.greenhouse.connect.providers;

import org.springframework.social.tripit.TripItOperations;
import org.springframework.social.tripit.TripItTemplate;

import com.springsource.greenhouse.connect.AbstractAccountProvider;
import com.springsource.greenhouse.connect.AccountConnectionRepository;
import com.springsource.greenhouse.connect.AccountProviderParameters;
import com.springsource.greenhouse.connect.OAuthToken;

public final class TripItAccountProvider extends AbstractAccountProvider<TripItOperations> {
	
	public TripItAccountProvider(AccountProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	protected TripItOperations createApi(OAuthToken accessToken) {
		if (accessToken == null) {
			throw new IllegalStateException("Cannot access TripIt without an access token");
		}
		return new TripItTemplate(getApiKey(), getSecret(), accessToken.getValue(), accessToken.getSecret());
	}

	protected String getProviderAccountId(TripItOperations api) {
		return api.getProfileId();
	}

	protected String getProviderProfileUrl(TripItOperations api) {
		return api.getProfileUrl();
	}
	
}