package com.springsource.greenhouse.connect;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.encrypt.StringEncryptor;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.social.twitter.TwitterTemplate;

import com.springsource.greenhouse.account.AccountMapper;

public final class TwitterAccountProvider extends JdbcAccountProvider<TwitterOperations> {
	TwitterAccountProvider(AccountProviderParameters parameters, JdbcTemplate jdbcTemplate, StringEncryptor encryptor, AccountMapper accountMapper) {
		super(parameters, jdbcTemplate, encryptor, accountMapper);
	}

	public TwitterOperations createApi(OAuthToken accessToken) {
		return accessToken != null ? new TwitterTemplate(getApiKey(), getSecret(), accessToken.getValue(), accessToken.getSecret()) : new TwitterTemplate();
	}

	public String getProviderAccountId(TwitterOperations api) {
		return api.getProfileId();
	}

	public String getProviderProfileUrl(TwitterOperations api) {
		return "http://www.twitter.com/" + api.getProfileId();
	}
}