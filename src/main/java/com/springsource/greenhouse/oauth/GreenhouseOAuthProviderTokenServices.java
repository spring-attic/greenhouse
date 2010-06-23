package com.springsource.greenhouse.oauth;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenImpl;
import org.springframework.security.oauth.provider.token.RandomValueProviderTokenServices;

// we need to think through persistence here... are we sure we want to be storing oauth tokens and access tokens in the same table?
public class GreenhouseOAuthProviderTokenServices extends RandomValueProviderTokenServices {
	
	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public GreenhouseOAuthProviderTokenServices(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
    @Override
    protected OAuthProviderTokenImpl readToken(String tokenValue) {
		return this.jdbcTemplate.queryForObject("select access from UserApp where token = ?", new RowMapper<OAuthProviderTokenImpl>() {
			public OAuthProviderTokenImpl mapRow(ResultSet rs, int rowNum) throws SQLException {
				OAuthProviderTokenImpl token = new OAuthProviderTokenImpl();
				token.setAccessToken(rs.getBoolean("access"));
				return token;
			}
			
		}, tokenValue);
    }

    @Override
    protected OAuthProviderTokenImpl removeToken(String tokenValue) {
    	this.jdbcTemplate.update("delete from UserApp where token = ?", tokenValue);
    	return null;
    }

    @Override
    protected void storeToken(String tokenValue, OAuthProviderTokenImpl token) {
    	// how to associate with user if its an oauth token?
    	this.jdbcTemplate.update("insert (token, access) into UserApp values (?, ?)", tokenValue, token.isAccessToken());    	
    }

}
