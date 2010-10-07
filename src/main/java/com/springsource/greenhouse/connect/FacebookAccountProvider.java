package com.springsource.greenhouse.connect;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.encrypt.StringEncryptor;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.facebook.FacebookTemplate;

import com.springsource.greenhouse.account.AccountMapper;

public final class FacebookAccountProvider extends JdbcAccountProvider<FacebookOperations> {
	FacebookAccountProvider(AccountProviderParameters parameters, JdbcTemplate jdbcTemplate, StringEncryptor encryptor, AccountMapper accountMapper) {
		super(parameters, jdbcTemplate, encryptor, accountMapper);
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