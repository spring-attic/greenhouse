package com.springsource.greenhouse.oauth;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;
import org.springframework.stereotype.Component;

@Component
public class OAuthUtil {
	private OAuthConsumerTokenServicesFactory tokenServicesFactory;
	
	@Inject
	public OAuthUtil(OAuthConsumerTokenServicesFactory tokenServicesFactory) {
		this.tokenServicesFactory = tokenServicesFactory;
	}
	
	public OAuthConsumerToken getAccessToken(String resourceId, HttpServletRequest request, Authentication authentication) {
		OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
		OAuthConsumerToken accessToken = tokenServices.getToken(resourceId);
		return accessToken;
	}
	
	public void removeToken(String resourceId, HttpServletRequest request, Authentication authentication) {
		NetworkConnectionsTokenServices tokenServices = 
				(NetworkConnectionsTokenServices) tokenServicesFactory.getTokenServices(authentication, request);
		tokenServices.removeToken(resourceId);
	}
}
