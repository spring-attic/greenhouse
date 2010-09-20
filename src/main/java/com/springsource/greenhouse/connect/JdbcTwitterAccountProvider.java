package com.springsource.greenhouse.connect;

import org.springframework.data.FileStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.social.twitter.TwitterTemplate;

class JdbcTwitterAccountProvider extends JdbcAccountProvider implements TwitterAccountProvider {

	public JdbcTwitterAccountProvider(String name, String apiKey, String secret, String requestTokenUrl,
			String authorizeUrl, String accessTokenUrl, JdbcTemplate jdbcTemplate, FileStorage pictureStorage,
			String profileUrlTemplate) {
		super(name, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl, jdbcTemplate, pictureStorage,
				profileUrlTemplate);
	}

	public TwitterOperations getTwitterApi(Long accountId) {
		return new TwitterTemplate(getApi(accountId));
	}

}
