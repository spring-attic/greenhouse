package com.springsource.greenhouse.settings;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DuplicateKeyException;
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
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.utils.MemberUtils;
import com.springsource.greenhouse.utils.SecurityUtils;

@Controller
@RequestMapping("/settings")
public class TwitterSettingsController {
	
	private static final String TWITTER = "twitter";
	private static final String UPDATE_CONNECTED_ACCOUNT_ID = 
		"update ConnectedAccount set accountId = ? where member = ? and provider = ?";

	private OAuthConsumerTokenServicesHelper oauthHelper;

	private TwitterOperations twitterService;
	
	private JdbcTemplate jdbcTemplate;

	private final AccountRepository accountRepository;
	
	@Inject
	public TwitterSettingsController(OAuthConsumerTokenServicesFactory oauthTokenFactory, 
			TwitterOperations twitterService, JdbcTemplate jdbcTemplate, AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
		this.oauthHelper = new OAuthConsumerTokenServicesHelper(oauthTokenFactory);
		this.jdbcTemplate = jdbcTemplate;
		this.twitterService = twitterService;
	}

	@RequestMapping(value="/twitter", method=RequestMethod.GET)
	public String connectView(Account account) {
		if (accountRepository.isConnected(account.getId(), TWITTER)) {
			return "settings/twitterConnected";
		} else {
			return "settings/twitterConnect";
		}
	}

	// TODO - change this callback url to /twitter/authorize and simplify @RequestMapping values
	@RequestMapping("/twitter/authorize")
	public String authorize(HttpServletRequest request, Authentication authentication) {
		String oauthToken = request.getParameter("oauth_token");
		if (oauthToken != null && oauthToken.length() > 0) {
			OAuthConsumerToken accessToken = oauthHelper.getAccessToken(TWITTER, request, authentication);
			Account account = (Account) authentication.getPrincipal();
			
			String screenName = twitterService.getScreenName(accessToken);
			makeTwitterScreenameGreenhouseUsername(screenName, account);
			jdbcTemplate.update(UPDATE_CONNECTED_ACCOUNT_ID, screenName, account.getId(), TWITTER);
			
			if (request.getParameter("tweetIt") != null) {
				// TODO should this be done asynchronously?
				tweetConnection(accessToken, request, account);
			}
			FlashMap.setSuccessMessage("Your Twitter account is now linked to your Greenhouse account!");
		}
		return "redirect:/settings/twitter";
	}

	@RequestMapping(value="/twitter", method=RequestMethod.DELETE)
	public String disconnectTwitter(Account account, HttpServletRequest request, Authentication authentication) {
		oauthHelper.removeToken(TWITTER, request, authentication);
		return "redirect:/settings/twitter";
	}

	// internal helpers
	private void tweetConnection(OAuthConsumerToken accessToken, HttpServletRequest request, Account account) {
		String message = "Linked with the Greenhouse at " + MemberUtils.assembleMemberProfileUrl(request, account);
		twitterService.updateStatus(accessToken, message);
	}

	private void makeTwitterScreenameGreenhouseUsername(String screenName, Account account) {
		try {
			jdbcTemplate.update("update Member set username = ? where id = ?", screenName, account.getId());
			SecurityUtils.signin(account.newUsername(screenName));
		} catch (DuplicateKeyException e) {
			// TODO add an info message that gets displayed under the success message
		}
	}
	
}