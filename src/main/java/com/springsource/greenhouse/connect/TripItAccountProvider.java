package com.springsource.greenhouse.connect;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.encrypt.StringEncryptor;
import org.springframework.social.tripit.TripItOperations;
import org.springframework.social.tripit.TripItTemplate;

import com.springsource.greenhouse.account.AccountMapper;

public final class TripItAccountProvider extends JdbcAccountProvider<TripItOperations> {
	TripItAccountProvider(AccountProviderParameters parameters, JdbcTemplate jdbcTemplate, StringEncryptor encryptor, AccountMapper accountMapper) {
		super(parameters, jdbcTemplate, encryptor, accountMapper);
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