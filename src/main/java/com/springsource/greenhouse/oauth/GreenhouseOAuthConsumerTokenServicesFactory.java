package com.springsource.greenhouse.oauth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;

public class GreenhouseOAuthConsumerTokenServicesFactory implements OAuthConsumerTokenServicesFactory {
  
  public OAuthConsumerTokenServices getTokenServices(Authentication authentication,
                                                     HttpServletRequest request) {
      return new GreenhouseOAuthConsumerTokenServices(request.getSession(true), authentication.getPrincipal().toString());
  }
}
