package com.springsource.greenhouse.settings;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TwitterController {
	private OAuthConsumerTokenServicesFactory tokenServicesFactory;
	private TwitterService twitterService;

	@Inject
	public TwitterController(OAuthConsumerTokenServicesFactory tokenServicesFactory, TwitterService twitterService) {
		this.tokenServicesFactory = tokenServicesFactory;
		this.twitterService = twitterService;		
	}
	
	@RequestMapping(value="/import/twitter", method=RequestMethod.GET)
	public String displayFriendFinderForm(HttpServletRequest request, Authentication authentication, 
			Map<String, Object> model) {
		OAuthConsumerToken accessToken = getAccessToken(request, authentication);
		model.put("twitterName", twitterService.getScreenName(accessToken));
		return "twitter/friendSearch";
	}
	
	@RequestMapping(value="/import/twitter", method=RequestMethod.POST)
	public String findFriends(HttpServletRequest request, Authentication authentication, @RequestParam("twitterName") String twitterName,
			Map<String, Object> model) {
		OAuthConsumerToken accessToken = getAccessToken(request, authentication);
		List<TwitterUser> friends = twitterService.getFriends(accessToken, twitterName);
		model.put("friends", friends);
		return "twitter/friends";
	}
	
	// TODO: This is duplicated here and in TwitterSettingsController. Find a common place for it.
	private OAuthConsumerToken getAccessToken(HttpServletRequest request, Authentication authentication) {
		OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
		OAuthConsumerToken accessToken = tokenServices.getToken("twitter");
		return accessToken;
	}
}
