package com.springsource.greenhouse.oauth;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;

import com.springsource.greenhouse.account.Account;

public class JdbcOAuthConsumerTokenServicesFactory implements OAuthConsumerTokenServicesFactory {
  
  private JdbcTemplate jdbcTemplate;

  @Inject
  public JdbcOAuthConsumerTokenServicesFactory(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
  }
    
  public OAuthConsumerTokenServices getTokenServices(Authentication authentication, HttpServletRequest request) {
	  Account account = (Account) authentication.getPrincipal();
      return new JdbcOAuthConsumerTokenServices(jdbcTemplate, request.getSession(true), account.getId());
  }
}
