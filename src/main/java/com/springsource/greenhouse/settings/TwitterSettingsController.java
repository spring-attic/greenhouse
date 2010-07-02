package com.springsource.greenhouse.settings;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.oauth.OAuthUtil;
import com.springsource.greenhouse.signin.GreenhouseUserDetails;

@Controller
@RequestMapping("/settings")
public class TwitterSettingsController {
	private static final String LINKED_TO_TWITTER_MESSAGE = "Signed up at the Greenhouse at ";

	private TwitterService twitterService;
	private JdbcTemplate jdbcTemplate;
	private OAuthUtil oauthUtil;

	@Inject
	public TwitterSettingsController(OAuthUtil oauthUtil, TwitterService twitterService, JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.twitterService = twitterService;
		this.oauthUtil = oauthUtil;
	}

	@RequestMapping(value = "/twitter", method = RequestMethod.GET)
	public String connectView(GreenhouseUserDetails currentUser) {
		if (isConnected(currentUser)) {
			return "settings/twitterConnected";
		} else {
			return "settings/twitterConnect";
		}
	}

	@RequestMapping("/twitterconnect/authorize")
	public String authorize(HttpServletRequest request, Authentication authentication,
	        @RequestParam(value = "tweetIt", required = false) boolean tweetIt) {
		String oauthToken = request.getParameter("oauth_token");
		if (oauthToken != null && oauthToken.length() > 0) {
			OAuthConsumerToken accessToken = oauthUtil.getAccessToken("twitter", request, authentication);

			GreenhouseUserDetails userDetails = (GreenhouseUserDetails) authentication.getPrincipal();
			if (tweetIt) {
				tweetAboutConnection(accessToken, request, userDetails);
			}
			setUserNameToTwitterName(accessToken, userDetails);
			FlashMap.getCurrent(request).put("connectedMessage",
			        "Your Twitter account is now linked to your Greenhouse account!");
		}
		return "redirect:/settings/twitter";
	}

	@RequestMapping(value = "/twitter", method = RequestMethod.DELETE)
	public String disconnectTwitter(GreenhouseUserDetails currentUser, HttpServletRequest request,
	        Authentication authentication) {
		oauthUtil.removeToken("twitter", request, authentication);
		return "redirect:/settings/twitter";
	}

	@ExceptionHandler(HttpStatusCodeException.class)
	public String handleIOException(HttpStatusCodeException ex, HttpServletRequest request) {
		request.setAttribute("error", ex.getMessage());
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		ex.printStackTrace(printWriter);
		request.setAttribute("stackTrace", stringWriter.getBuffer().toString());
		return "twitter/error";
	}
	
	// internal helpers
	private boolean isConnected(GreenhouseUserDetails currentUser) {
		return jdbcTemplate.queryForInt(
		        "select count(*) from NetworkConnection where userId = ? and network = 'twitter'", 
		        currentUser.getEntityId()) == 1;
	}

	private String assembleMemberProfileUrl(HttpServletRequest request, GreenhouseUserDetails userDetails) {
		String userKey = userDetails.getProfileKey();
		int serverPort = request.getServerPort();
		String portPart = serverPort == 80 || serverPort == 443 ? "" : ":" + serverPort;
		return request.getScheme() + "://" + request.getServerName() + portPart + request.getContextPath()
		        + "/members/" + userKey;
	}

	private void setUserNameToTwitterName(OAuthConsumerToken accessToken, GreenhouseUserDetails userDetails) {
		String twitterName = twitterService.getScreenName(accessToken);
		userDetails.setUsername(twitterName);
		jdbcTemplate.update("update User set userName = ? where id = ?", twitterName, userDetails.getEntityId());
	}

	private void tweetAboutConnection(OAuthConsumerToken accessToken, HttpServletRequest request,
	        GreenhouseUserDetails userDetails) {
		if (accessToken.isAccessToken()) {
			String message = LINKED_TO_TWITTER_MESSAGE + assembleMemberProfileUrl(request, userDetails);
			twitterService.updateStatus(accessToken, message);
		}
	}
}