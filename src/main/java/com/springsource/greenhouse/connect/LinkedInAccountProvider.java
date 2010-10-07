package com.springsource.greenhouse.connect;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.encrypt.StringEncryptor;
import org.springframework.social.linkedin.LinkedInOperations;
import org.springframework.social.linkedin.LinkedInTemplate;

import com.springsource.greenhouse.account.AccountMapper;

public final class LinkedInAccountProvider extends JdbcAccountProvider<LinkedInOperations> {
	LinkedInAccountProvider(AccountProviderParameters parameters, JdbcTemplate jdbcTemplate, StringEncryptor encryptor, AccountMapper accountMapper) {
		super(parameters, jdbcTemplate, encryptor, accountMapper);
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
}