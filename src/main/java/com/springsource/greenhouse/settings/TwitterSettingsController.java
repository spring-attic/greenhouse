package com.springsource.greenhouse.settings;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.oauth.extras.OAuthConsumerTokenServicesHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.signin.GreenhouseUserDetails;

@Controller
@RequestMapping("/settings")
public class TwitterSettingsController {
	
	private OAuthConsumerTokenServicesHelper oauthHelper;

	private TwitterOperations twitterService;
	
	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public TwitterSettingsController(OAuthConsumerTokenServicesFactory oauthTokenFactory, TwitterOperations twitterService, JdbcTemplate jdbcTemplate) {
		this.oauthHelper = new OAuthConsumerTokenServicesHelper(oauthTokenFactory);
		this.jdbcTemplate = jdbcTemplate;
		this.twitterService = twitterService;
	}

	@RequestMapping(value="/twitter", method=RequestMethod.GET)
	public String connectView(GreenhouseUserDetails currentUser) {
		if (isConnected(currentUser)) {
			return "settings/twitterConnected";
		} else {
			return "settings/twitterConnect";
		}
	}

	// TODO - change this callback url to /twitter/authorize and simplify @RequestMapping values
	@RequestMapping("/twitterconnect/authorize")
	public String authorize(HttpServletRequest request, Authentication authentication) {
		String oauthToken = request.getParameter("oauth_token");
		if (oauthToken != null && oauthToken.length() > 0) {
			OAuthConsumerToken accessToken = oauthHelper.getAccessToken("twitter", request, authentication);
			GreenhouseUserDetails userDetails = (GreenhouseUserDetails) authentication.getPrincipal();
			makeTwitterScreenameGreenhouseUsername(accessToken, userDetails);
			if (request.getParameter("tweetIt") != null) {
				tweetConnection(accessToken, request, userDetails);
			}
			FlashMap.getCurrent(request).put("connectedMessage", "Your Twitter account is now linked to your Greenhouse account!");
		}
		return "redirect:/settings/twitter";
	}

	@RequestMapping(value="/twitter", method=RequestMethod.DELETE)
	public String disconnectTwitter(GreenhouseUserDetails currentUser, HttpServletRequest request, Authentication authentication) {
		oauthHelper.removeToken("twitter", request, authentication);
		return "redirect:/settings/twitter";
	}

	// internal helpers

	private boolean isConnected(GreenhouseUserDetails currentUser) {
		return jdbcTemplate.queryForInt("select count(*) from NetworkConnection where userId = ? and network = 'twitter'", currentUser.getEntityId()) == 1;
	}

	private void tweetConnection(OAuthConsumerToken accessToken, HttpServletRequest request, GreenhouseUserDetails userDetails) {
		String message = "Signed up at the Greenhouse at " + assembleMemberProfileUrl(request, userDetails);
		twitterService.updateStatus(accessToken, message);
	}
	
	private String assembleMemberProfileUrl(HttpServletRequest request, GreenhouseUserDetails userDetails) {
		String userKey = userDetails.getProfileKey();
		int serverPort = request.getServerPort();
		String portPart = serverPort == 80 || serverPort == 443 ? "" : ":" + serverPort;
		return request.getScheme() + "://" + request.getServerName() + portPart + request.getContextPath() + "/members/" + userKey;
	}

	private void makeTwitterScreenameGreenhouseUsername(OAuthConsumerToken accessToken, GreenhouseUserDetails userDetails) {
		String screenName = twitterService.getScreenName(accessToken);
		jdbcTemplate.update("update User set username = ? where id = ?", screenName, userDetails.getEntityId());
		userDetails.setUsername(screenName);
	}
	
}