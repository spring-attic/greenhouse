package com.springsource.greenhouse.connect;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.social.twitter.TwitterTemplate;

import com.springsource.greenhouse.account.AccountMapper;

class JdbcTwitterAccountProvider extends JdbcAccountProvider implements TwitterAccountProvider {

	public JdbcTwitterAccountProvider(String name, String apiKey, String secret, String requestTokenUrl,
			String authorizeUrl, String accessTokenUrl, JdbcTemplate jdbcTemplate, AccountMapper accountMapper) {
		super(name, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl, jdbcTemplate, accountMapper);
	}

	public TwitterOperations getTwitterApi(Long accountId) {
		return new TwitterTemplate(getApi(accountId));
	}

	public Token fetchNewRequestToken() {
		OAuthService service = new ServiceBuilder().apiKey(apiKey).apiSecret(secret).provider(TwitterApi.class).build();
		org.scribe.model.Token requestToken = service.getRequestToken();
		return new Token(requestToken.getToken(), requestToken.getSecret());
	}

	public Token fetchAccessToken(Token requestToken, String verifier) {
		org.scribe.model.Token scribeToken = new org.scribe.model.Token(requestToken.getValue(),
				requestToken.getValue());
		ServiceBuilder builder = new ServiceBuilder();
		org.scribe.model.Token accessToken = builder.apiKey(apiKey).apiSecret(secret).provider(TwitterApi.class)
				.build().getAccessToken(scribeToken, new Verifier(verifier));
		return new Token(accessToken.getToken(), accessToken.getSecret());
	}
}
