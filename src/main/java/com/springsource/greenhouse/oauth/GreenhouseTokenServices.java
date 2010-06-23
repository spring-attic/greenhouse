package com.springsource.greenhouse.oauth;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenImpl;
import org.springframework.security.oauth.provider.token.RandomValueProviderTokenServices;

public class GreenhouseTokenServices extends RandomValueProviderTokenServices {
	
	@SuppressWarnings("unused")
	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public GreenhouseTokenServices(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
    @Override
    protected OAuthProviderTokenImpl readToken(String tokenValue) {
		// TODO build token from DB using JdbcTemplate
    	return null;
    }

    @Override
    protected OAuthProviderTokenImpl removeToken(String tokenValue) {
		// TODO remove token from DB using JdbcTemplate
    	return null;
    }

    @Override
    protected void storeToken(String tokenValue, OAuthProviderTokenImpl token) {
		// TODO save token to DB using JdbcTemplate
    }

}
