package com.springsource.greenhouse.oauth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth.common.OAuthException;
import org.springframework.security.oauth.common.signature.SharedConsumerSecret;
import org.springframework.security.oauth.provider.BaseConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;

public class GreenhouseConsumerDetailsService implements ConsumerDetailsService {

	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public GreenhouseConsumerDetailsService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public ConsumerDetails loadConsumerByConsumerKey(final String key) throws OAuthException {
		return jdbcTemplate.queryForObject("select name, secret from Consumer where consumerKey = ?", new RowMapper<ConsumerDetails>() {
			public ConsumerDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				BaseConsumerDetails consumerDetails = new BaseConsumerDetails();
				consumerDetails.setConsumerName(rs.getString("name"));
				consumerDetails.setConsumerKey(key);
				consumerDetails.setSignatureSecret(new SharedConsumerSecret(rs.getString("secret")));
		        consumerDetails.setResourceName("Your Greenhouse Profile");
		        consumerDetails.setResourceDescription("Everything Greenhouse knows about you.");
		        consumerDetails.setAuthorities(new ArrayList<GrantedAuthority>());        
				return consumerDetails;
			}
		}, key);
	}

}
