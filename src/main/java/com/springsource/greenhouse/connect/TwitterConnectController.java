package com.springsource.greenhouse.connect;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.social.twitter.DuplicateTweetException;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;

@Controller
@RequestMapping("/connect/twitter")
public class TwitterConnectController {
	
	private static final String OAUTH_TOKEN_ATTRIBUTE = "oauthToken";
	
	private AccountProvider<TwitterOperations> accountProvider;

	@Inject
	public TwitterConnectController(AccountProvider<TwitterOperations> accountProvider) {
		this.accountProvider = accountProvider;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String twitterConnect(Account account) {
		if (accountProvider.isConnected(account.getId())) {
			return "connect/twitterConnected";
		} else {
			return "connect/twitterConnect";
		}
	}

	@RequestMapping(method=RequestMethod.POST)
	public String connect(@RequestParam boolean tweetIt, WebRequest request) {
		OAuthToken requestToken = accountProvider.getRequestToken();
		request.setAttribute(OAUTH_TOKEN_ATTRIBUTE, requestToken, WebRequest.SCOPE_SESSION);
		return "redirect:" + accountProvider.getAuthorizeUrl(requestToken.getValue());
	}

	@RequestMapping(method=RequestMethod.GET, params="oauth_token")
	public String authorizeCallback(@RequestParam("oauth_token") String token, @RequestParam("oauth_verifier") String verifier, @RequestParam boolean postTweet, Account account, WebRequest request) {
		OAuthToken requestToken = (OAuthToken) request.getAttribute(OAUTH_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		if (requestToken == null) {
			return "connect/twitterConnect";
		}
		request.removeAttribute(OAUTH_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		TwitterOperations twitter = accountProvider.connect(account.getId(), requestToken, verifier);
		accountProvider.updateProviderAccountId(account.getId(), twitter.getScreenName());
		if (postTweet) {
			try {
				twitter.tweet("Join me at the Greenhouse! " + account.getProfileUrl());
			} catch (DuplicateTweetException e) {
			}
		}
		FlashMap.setSuccessMessage("Your Greenhouse account is now connected to your Twitter account!");
		return "redirect:/connect/twitter";
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public String disconnectTwitter(Account account, HttpServletRequest request, Authentication authentication) {
		accountProvider.disconnect(account.getId());
		return "redirect:/connect/twitter";
	}

}