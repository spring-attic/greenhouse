package com.springsource.greenhouse.connect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.social.facebook.FacebookRequestSigner;
import org.springframework.social.oauth.OAuthClientRequestSigner;
import org.springframework.social.oauth.OAuthSigningClientHttpRequestFactory;
import org.springframework.social.oauth1.ScribeOAuth1RequestSigner;
import org.springframework.social.twitter.TwitterErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

public class JdbcAccountProvider extends AccountProvider {
	private final JdbcTemplate jdbcTemplate;

	public JdbcAccountProvider(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public RestOperations getAccountConnectionOperations(Long accountId) {
		return jdbcTemplate.queryForObject(
				"select accessToken, secret, provider from AccountConnection where provider = ? and member = ?",
				new RowMapper<RestOperations>() {
					public RestOperations mapRow(ResultSet rs, int row) throws SQLException {
						RestTemplate restTemplate = new RestTemplate(new OAuthSigningClientHttpRequestFactory(getRequestSigner(
								rs.getString("accessToken"), rs.getString("secret"))));
						restTemplate.setErrorHandler(new TwitterErrorHandler());

						// Go figure: Facebook uses "text/javascript" as the
						// JSON content type
						MappingJacksonHttpMessageConverter jsonMessageConverter = new MappingJacksonHttpMessageConverter();
						jsonMessageConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "javascript")));
						restTemplate.getMessageConverters().add(jsonMessageConverter);

						return restTemplate;
					}
				}, getName(), accountId);
	}

	private OAuthClientRequestSigner getRequestSigner(String accessToken, String accessTokenSecret) {
		// TODO: Dumb selection approach...make smarter later
		if (getName().equals("Facebook")) {
			return new FacebookRequestSigner(accessToken);
		}

		return new ScribeOAuth1RequestSigner(getApiKey(), getApiSecret(), accessToken, accessTokenSecret);
	}
}
