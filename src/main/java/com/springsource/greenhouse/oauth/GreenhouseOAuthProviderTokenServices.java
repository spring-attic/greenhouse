package com.springsource.greenhouse.oauth;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;

// we need to think through persistence here... are we sure we want to be storing oauth tokens and access tokens in the same table?
public class GreenhouseOAuthProviderTokenServices implements OAuthProviderTokenServices {
	
	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public GreenhouseOAuthProviderTokenServices(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public OAuthProviderToken createUnauthorizedRequestToken(String consumerKey, String callbackUrl) throws AuthenticationException {
		return null;
	}

	public void authorizeRequestToken(String requestToken, String verifier, Authentication authentication) throws AuthenticationException {
	}
	
	public OAuthAccessProviderToken createAccessToken(String requestToken) throws AuthenticationException {
		return null;
	}	
	
	public OAuthProviderToken getToken(String token) throws AuthenticationException {
		return null;
	}

}
