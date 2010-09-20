package com.springsource.greenhouse.connect;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.social.twitter.TwitterOperations;

class JdbcTwitterAccountProvider extends JdbcAccountProvider implements TwitterAccountProvider {

	public JdbcTwitterAccountProvider(String name, String apiKey, String secret, String requestTokenUrl,
			String authorizeUrl, String accessTokenUrl, JdbcTemplate jdbcTemplate) {
		super(name, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl, jdbcTemplate);
	}

	public TwitterOperations getTwitterApi(Long accountId) {
		// TODO
		return null;
	}

}
