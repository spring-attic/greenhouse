package com.springsource.greenhouse.settings;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;
import org.springframework.security.oauth.extras.OAuthConsumerTokenServicesHelper;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;

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
	public String connectView(Account account) {
		if (isConnected(account)) {
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
			Account account = (Account) authentication.getPrincipal();
			makeTwitterScreenameGreenhouseUsername(accessToken, account);
			if (request.getParameter("tweetIt") != null) {
				tweetConnection(accessToken, request, account);
			}
			FlashMap.setSuccessMessage("Your Twitter account is now linked to your Greenhouse account!");
		}
		return "redirect:/settings/twitter";
	}

	@RequestMapping(value="/twitter", method=RequestMethod.DELETE)
	public String disconnectTwitter(Account account, HttpServletRequest request, Authentication authentication) {
		oauthHelper.removeToken("twitter", request, authentication);
		return "redirect:/settings/twitter";
	}

	// internal helpers

	private boolean isConnected(Account account) {
		return jdbcTemplate.queryForInt("select count(*) from ConnectedAccount where member = ? and accountName = 'twitter'", account.getId()) == 1;
	}

	private void tweetConnection(OAuthConsumerToken accessToken, HttpServletRequest request, Account account) {
		String message = "Signed up at the Greenhouse at " + assembleMemberProfileUrl(request, account);
		twitterService.updateStatus(accessToken, message);
	}
	
	private String assembleMemberProfileUrl(HttpServletRequest request, Account account) {
		String profileKey = account.getMemberProfileKey();
		int serverPort = request.getServerPort();
		String portPart = serverPort == 80 || serverPort == 443 ? "" : ":" + serverPort;
		return request.getScheme() + "://" + request.getServerName() + portPart + request.getContextPath() + "/members/" + profileKey;
	}

	private void makeTwitterScreenameGreenhouseUsername(OAuthConsumerToken accessToken, Account account) {
		String screenName = twitterService.getScreenName(accessToken);
		jdbcTemplate.update("update Member set username = ? where id = ?", screenName, account.getId());
		// TODO clone Account with new username, set using SecurityContext
	}
	
}