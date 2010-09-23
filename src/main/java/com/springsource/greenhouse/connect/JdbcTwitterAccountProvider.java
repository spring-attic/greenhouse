package com.springsource.greenhouse.connect;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.social.twitter.TwitterTemplate;

import com.springsource.greenhouse.account.AccountMapper;

class JdbcTwitterAccountProvider extends JdbcAccountProvider implements TwitterAccountProvider {

	public JdbcTwitterAccountProvider(String name, String apiKey, String secret, String requestTokenUrl, String authorizeUrl, String accessTokenUrl, JdbcTemplate jdbcTemplate, AccountMapper accountMapper) {
		super(name, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl, jdbcTemplate, accountMapper);
	}

	public TwitterOperations getTwitterApi(Long accountId) {
		return new TwitterTemplate(getApi(accountId));
	}

}
