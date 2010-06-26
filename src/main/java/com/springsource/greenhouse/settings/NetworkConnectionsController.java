package com.springsource.greenhouse.settings;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.signin.GreenhouseUserDetails;

@Controller
@RequestMapping("/settings")
public class NetworkConnectionsController {
    
    private OAuthConsumerTokenServicesFactory tokenServicesFactory;

    @Inject
    public NetworkConnectionsController(OAuthConsumerTokenServicesFactory tokenServicesFactory) {
		this.tokenServicesFactory = tokenServicesFactory;
	}

	@RequestMapping(value="/twitter", method=RequestMethod.GET)
    public String twitterConnectView(Authentication auth) {
		if (isConnected((GreenhouseUserDetails) auth.getPrincipal())) {
			return "settings/twitterConnected";
		} else {
			return "settings/twitterConnect";
		}
	}
    
	@RequestMapping("/twitterconnect/authorize")
    public String authorizeTwitter(HttpServletRequest request, Authentication authentication) {
        String oauthToken = request.getParameter("oauth_token");      
        if (oauthToken != null && oauthToken.length() > 0) {
            String oauthVerifier = request.getParameter("oauth_verifier");
            OAuthConsumerToken token = new OAuthConsumerToken();
            token.setAccessToken(true);
            token.setResourceId("twitter");
            token.setValue(oauthToken);
            token.setSecret(oauthVerifier);          
            tokenServicesFactory.getTokenServices(authentication, request).storeToken("twitter", token);
            FlashMap.getCurrent(request).put("connectedMessage", "Your Twitter account is now linked to your Greenhouse account!");
        }
        return "redirect:/settings/twitter";
    }
	
	@RequestMapping(value="/twitter", method=RequestMethod.DELETE)
    public String disconnectTwitter(Authentication auth) {
		// TODO
		return "redirect:/settings/twitter";
	}

	// internal helpers
	
    private boolean isConnected(GreenhouseUserDetails principal) {
    	// TODO
		return false;
	}

}