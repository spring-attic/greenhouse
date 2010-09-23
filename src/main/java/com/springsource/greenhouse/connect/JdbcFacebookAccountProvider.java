package com.springsource.greenhouse.connect;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.facebook.FacebookRequestSigner;
import org.springframework.social.facebook.FacebookTemplate;
import org.springframework.social.oauth.OAuthClientRequestSigner;

import com.springsource.greenhouse.account.AccountMapper;

class JdbcFacebookAccountProvider extends JdbcAccountProvider implements FacebookAccountProvider {

	public JdbcFacebookAccountProvider(String name, String apiKey, String secret, String requestTokenUrl, String authorizeUrl, String accessTokenUrl, JdbcTemplate jdbcTemplate, AccountMapper accountMapper) {
		super(name, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl, jdbcTemplate, accountMapper);
	}

	public Long getAppId() {
		// TODO
		return null;
	}
	
	public FacebookOperations getFacebookApi(Long accountId) {
		return new FacebookTemplate(getApi(accountId));
	}

	@Override
	protected OAuthClientRequestSigner getRequestSigner(String accessToken, String accessTokenSecret) {
		return new FacebookRequestSigner(accessToken);
	}

}