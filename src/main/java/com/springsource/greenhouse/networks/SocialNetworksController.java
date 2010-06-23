package com.springsource.greenhouse.networks;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/networks")
public class SocialNetworksController {
  @RequestMapping("/twitter")
  public void authorizeTwitter(HttpServletRequest request, Authentication authentication) {
      // TODO: Seems that I shouldn't have to do this here. Should be a filter/aspect that does this for me.
      
      String oauthToken = request.getParameter("oauth_token");
      String oauthVerifier = request.getParameter("oauth_verifier");
      OAuthConsumerToken token = new OAuthConsumerToken();
      token.setAccessToken(true);
      token.setResourceId("twitter");
      token.setValue(oauthToken);
      token.setSecret(oauthVerifier);
      
      tokenServicesFactory.getTokenServices(authentication, request).storeToken("twitter", token);
      
  }
  
  @Autowired
  OAuthConsumerTokenServicesFactory tokenServicesFactory;
}