package org.springframework.security.oauth.extras;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;

import com.springsource.greenhouse.oauth.OAuthConsumerTokenServicesAdapter;

public class OAuthConsumerTokenServicesHelper {
	
	private OAuthConsumerTokenServicesFactory tokenServicesFactory;
	
	public OAuthConsumerTokenServicesHelper(OAuthConsumerTokenServicesFactory tokenServicesFactory) {
		this.tokenServicesFactory = tokenServicesFactory;
	}
	
	public OAuthConsumerToken getAccessToken(String resourceId, HttpServletRequest request, Authentication authentication) {
		OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
		OAuthConsumerToken accessToken = tokenServices.getToken(resourceId);
		return accessToken;
	}
	
	public void removeToken(String resourceId, HttpServletRequest request, Authentication authentication) {
		OAuthConsumerTokenServicesAdapter tokenServices = 
				(OAuthConsumerTokenServicesAdapter) tokenServicesFactory.getTokenServices(authentication, request);
		tokenServices.removeToken(resourceId);
	}
}
