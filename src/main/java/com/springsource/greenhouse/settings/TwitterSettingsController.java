package com.springsource.greenhouse.settings;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.connect.TwitterAccountProvider;

@Controller
@RequestMapping("/settings/twitter")
public class TwitterSettingsController {
	
	private final TwitterAccountProvider accountProvider;
	
	@Inject
	public TwitterSettingsController(TwitterAccountProvider accountProvider) {
		this.accountProvider = accountProvider;
	}

	@RequestMapping(method=RequestMethod.GET)
	public String connectView(Account account) {
		if (accountProvider.isConnected(account.getId())) {
			return "settings/twitterConnected";
		} else {
			return "settings/twitterConnect";
		}
	}

	@RequestMapping("/authorize")
	public String authorize(HttpServletRequest request, Account account) {
		String oauthToken = request.getParameter("oauth_token");
		if (oauthToken != null && oauthToken.length() > 0) {
			TwitterOperations twitterApi = accountProvider.getTwitterApi(account.getId());
			String screenName = twitterApi.getScreenName();
			accountProvider.saveProviderAccountId(account.getId(), screenName);
			if (request.getParameter("tweetIt") != null) {
				twitterApi.tweet("Join me at the Greenhouse! " + account.getProfileUrl());
			}
			FlashMap.setSuccessMessage("Your Greenhouse account is now connected to your Twitter account!");
		}
		return "redirect:/settings/twitter";
	}

	@RequestMapping(method=RequestMethod.DELETE)
	public String disconnectTwitter(Account account, HttpServletRequest request, Authentication authentication) {
		accountProvider.disconnect(account.getId());
		return "redirect:/settings/twitter";
	}

}