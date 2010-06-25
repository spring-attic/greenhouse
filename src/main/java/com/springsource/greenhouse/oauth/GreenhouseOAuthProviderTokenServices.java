package com.springsource.greenhouse.oauth;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.codec.Base64;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenImpl;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;

import com.springsource.greenhouse.signin.GreenhouseUserDetails;

public class GreenhouseOAuthProviderTokenServices implements OAuthProviderTokenServices {
	
    private Random random = new SecureRandom();

	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public GreenhouseOAuthProviderTokenServices(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public OAuthProviderToken createUnauthorizedRequestToken(String consumerKey, String callbackUrl) throws AuthenticationException {
	    OAuthProviderTokenImpl token = new OAuthProviderTokenImpl();
	    token.setValue(UUID.randomUUID().toString());
	    token.setConsumerKey(consumerKey);
	    token.setSecret(generateSecret());	    
	    token.setCallbackUrl(callbackUrl);
	    token.setTimestamp(System.currentTimeMillis());
	    jdbcTemplate.update(INSERT_REQUEST_TOKEN_SQL, token.getValue(), token.getConsumerKey(), token.getSecret(), token.getCallbackUrl(), token.getTimestamp());
		return token;
	}

	public void authorizeRequestToken(String requestToken, String verifier, Authentication authentication) throws AuthenticationException {
		Long userId = ((GreenhouseUserDetails) authentication.getPrincipal()).getEntityId();
		jdbcTemplate.update(AUTHORIZE_REQUEST_TOKEN_SQL, verifier, System.currentTimeMillis(), userId, requestToken);
	}
	
	public OAuthAccessProviderToken createAccessToken(String requestToken) throws AuthenticationException {
		Map<String, Object> row = jdbcTemplate.queryForMap("select consumerKey, userId from OAuthToken where tokenValue = ?", requestToken);	
		jdbcTemplate.update(DELETE_REQUEST_TOKEN_SQL, requestToken);
		OAuthProviderTokenImpl token = new OAuthProviderTokenImpl();
	    token.setValue(UUID.randomUUID().toString());
	    token.setAccessToken(true);
	    token.setConsumerKey((String) row.get("consumerKey"));
	    token.setSecret(generateSecret());
	    jdbcTemplate.update(INSERT_ACCESS_TOKEN_SQL, row.get("userId"), token.getConsumerKey(), token.getValue());
		return token;
	}	
	
	public OAuthProviderToken getToken(final String token) throws AuthenticationException {
		if (jdbcTemplate.queryForInt("select count(*) from OAuthToken where tokenValue = ?", token) == 1) {
			return jdbcTemplate.queryForObject(SELECT_REQUEST_TOKEN_SQL, new RowMapper<OAuthProviderToken>() {
				public OAuthProviderToken mapRow(ResultSet rs, int rowNum) throws SQLException {
					OAuthProviderTokenImpl holder = new OAuthProviderTokenImpl();
					holder.setValue(token);
					holder.setConsumerKey(rs.getString("consumerKey"));
					holder.setSecret(rs.getString("secret"));
					holder.setCallbackUrl(rs.getString("callbackUrl"));
					holder.setTimestamp(rs.getLong("updateTimestamp"));
					holder.setVerifier(rs.getString("verifier"));
					return holder;
				}
			}, token);
		} else {
			return jdbcTemplate.queryForObject(SELECT_ACCESS_TOKEN_SQL, new RowMapper<OAuthProviderToken>() {
				public OAuthProviderToken mapRow(ResultSet rs, int rowNum) throws SQLException {
					OAuthProviderTokenImpl holder = new OAuthProviderTokenImpl();
					holder.setValue(token);
					holder.setAccessToken(true);
					holder.setConsumerKey(rs.getString("consumerKey"));
					Collection<GrantedAuthority> authorities = Collections.emptySet();
					holder.setUserAuthentication(new UsernamePasswordAuthenticationToken("doesnt-matter-yet", "doesnt-matter-yet", authorities));
					return holder;
				}
			}, token);			
		}
	}
	
	private String generateSecret() {
	    byte[] secretBytes = new byte[80];
	    random.nextBytes(secretBytes);
	    String secret = new String(Base64.encode(secretBytes));
	    return secret;
	}
	
    private static final String INSERT_REQUEST_TOKEN_SQL = "insert into OAuthToken (tokenValue, consumerKey, secret, callbackUrl, updateTimestamp) values (?, ?, ?, ?, ?)";
    private static final String AUTHORIZE_REQUEST_TOKEN_SQL = "update OAuthToken set verifier = ?, updateTimestamp = ?, userId = ? where tokenValue = ?";
    private static final String DELETE_REQUEST_TOKEN_SQL = "delete from OAuthToken where tokenValue = ?";
    private static final String INSERT_ACCESS_TOKEN_SQL = "insert into AuthorizedConsumer (userId, consumerKey, accessToken) values (?, ?, ?)";

    private static final String SELECT_REQUEST_TOKEN_SQL = "select consumerKey, secret, callbackUrl, updateTimestamp, verifier from OAuthToken where tokenValue = ?";
	private static final String SELECT_ACCESS_TOKEN_SQL = "select userId, consumerKey from AuthorizedConsumer where accessToken = ?";

}
