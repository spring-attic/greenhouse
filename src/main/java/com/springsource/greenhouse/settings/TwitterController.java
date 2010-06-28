package com.springsource.greenhouse.settings;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.signin.GreenhouseUserDetails;

@Controller
@RequestMapping("/settings")
public class TwitterController {
    
    private OAuthConsumerTokenServicesFactory tokenServicesFactory;

    private JdbcTemplate jdbcTemplate;
    
    @Inject
    public TwitterController(OAuthConsumerTokenServicesFactory tokenServicesFactory, JdbcTemplate jdbcTemplate) {
		this.tokenServicesFactory = tokenServicesFactory;
		this.jdbcTemplate = jdbcTemplate;
	}

	@RequestMapping(value="/twitter", method=RequestMethod.GET)
    public String connectView(Authentication auth) {
		if (isConnected((GreenhouseUserDetails) auth.getPrincipal())) {
			return "settings/twitterConnected";
		} else {
			return "settings/twitterConnect";
		}
	}
    
	@RequestMapping("/twitterconnect/authorize")
    public String authorize(HttpServletRequest request, Authentication authentication) {
        String oauthToken = request.getParameter("oauth_token");      
        if (oauthToken != null && oauthToken.length() > 0) {
            FlashMap.getCurrent(request).put("connectedMessage", "Your Twitter account is now linked to your Greenhouse account!");
        }
        return "redirect:/settings/twitter";
    }
	
	@RequestMapping(value="/twitter", method=RequestMethod.DELETE)
    public String disconnectTwitter(Authentication auth) {
		GreenhouseUserDetails principal = (GreenhouseUserDetails) auth.getPrincipal();
		jdbcTemplate.update("delete from NetworkConnection where userId = ? and network = 'twitter'", principal.getEntityId());
		return "redirect:/settings/twitter";
	}

	// internal helpers
	
    private boolean isConnected(GreenhouseUserDetails principal) {
    	return jdbcTemplate.queryForInt("select count(*) from NetworkConnection where userId = ? and network = 'twitter'", principal.getEntityId()) == 1;
	}

}