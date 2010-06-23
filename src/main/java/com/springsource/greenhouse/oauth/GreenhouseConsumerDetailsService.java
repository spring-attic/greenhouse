package com.springsource.greenhouse.oauth;

import java.util.ArrayList;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth.common.OAuthException;
import org.springframework.security.oauth.provider.BaseConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;

public class GreenhouseConsumerDetailsService implements ConsumerDetailsService {

	@SuppressWarnings("unused")
	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public GreenhouseConsumerDetailsService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public ConsumerDetails loadConsumerByConsumerKey(String key) throws OAuthException {
		BaseConsumerDetails consumerDetails = new BaseConsumerDetails();
		// TODO build consumer details from DB using JdbcTemplate
        consumerDetails.setResourceName("Your Greenhouse Profile");
        consumerDetails.setResourceDescription("Everything Greenhouse knows about you.");
        consumerDetails.setAuthorities(new ArrayList<GrantedAuthority>());        
		return consumerDetails;
	}

}
