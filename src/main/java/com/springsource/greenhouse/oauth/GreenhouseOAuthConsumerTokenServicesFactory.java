package com.springsource.greenhouse.oauth;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;

import com.springsource.greenhouse.signin.GreenhouseUserDetails;

public class GreenhouseOAuthConsumerTokenServicesFactory implements OAuthConsumerTokenServicesFactory {
  
  private JdbcTemplate jdbcTemplate;
    
  @Inject
  public GreenhouseOAuthConsumerTokenServicesFactory(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
  }
    
  public OAuthConsumerTokenServices getTokenServices(Authentication authentication,
                                                     HttpServletRequest request) {
      GreenhouseUserDetails userDetails = (GreenhouseUserDetails) authentication.getPrincipal();
      return new GreenhouseOAuthConsumerTokenServices(jdbcTemplate, request.getSession(true), userDetails.getUsername());
  }
}
