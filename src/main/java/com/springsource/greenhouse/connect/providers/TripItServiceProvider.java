package com.springsource.greenhouse.connect.providers;

import org.springframework.social.tripit.TripItOperations;
import org.springframework.social.tripit.TripItTemplate;

import com.springsource.greenhouse.connect.AbstractServiceProvider;
import com.springsource.greenhouse.connect.AccountConnectionRepository;
import com.springsource.greenhouse.connect.ServiceProviderParameters;
import com.springsource.greenhouse.connect.OAuthToken;

public final class TripItServiceProvider extends AbstractServiceProvider<TripItOperations> {
	
	public TripItServiceProvider(ServiceProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	protected TripItOperations createServiceOperations(OAuthToken accessToken) {
		if (accessToken == null) {
			throw new IllegalStateException("Cannot access TripIt without an access token");
		}
		return new TripItTemplate(getApiKey(), getSecret(), accessToken.getValue(), accessToken.getSecret());
	}

	protected String fetchProviderAccountId(TripItOperations tripIt) {
		return tripIt.getProfileId();
	}

	protected String buildProviderProfileUrl(String tripItId, TripItOperations tripIt) {
		return tripIt.getProfileUrl();
	}
	
}