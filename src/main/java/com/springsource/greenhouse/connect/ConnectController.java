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
@RequestMapping("/connect")
public class ConnectController {
	private final TwitterAccountProvider twitterProvider;

	@Inject
	public ConnectController(TwitterAccountProvider twitterProvider) {
		this.twitterProvider = twitterProvider;
	}

	@RequestMapping(value = "/twitter", method = RequestMethod.GET)
	public String twitterConnect(Account account) {
		if (twitterProvider.isConnected(account.getId())) {
			return "connect/twitterConnected";
		} else {
			return "connect/twitterConnect";
		}
	}

	@RequestMapping(value="/twitter", method=RequestMethod.POST)
	public String connectToTwitter(boolean tweetIt, WebRequest request) {
		Token requestToken = twitterProvider.fetchNewRequestToken(getCallbackUrl(tweetIt));
		request.setAttribute("requestToken", requestToken, WebRequest.SCOPE_SESSION);
		return "redirect:" + twitterProvider.getAuthorizeUrl() + "?oauth_token=" + requestToken.getValue();
	}

	@RequestMapping(value = "/twitter", method = RequestMethod.GET, params = "oauth_token")
	public String twitterCallback(@RequestParam("oauth_token") String token,
			@RequestParam("oauth_verifier") String verifier, Account account, boolean tweetIt, WebRequest request) {
		Token requestToken = (Token) request.getAttribute("requestToken", WebRequest.SCOPE_SESSION);
		if (requestToken == null) {
			return "connect/twitterConnect";
		}

		request.removeAttribute("requestToken", WebRequest.SCOPE_SESSION);
		Token accessToken = twitterProvider.fetchAccessToken(requestToken, verifier, getCallbackUrl(tweetIt));

		if (accessToken != null) {
			twitterProvider.connect(account.getId(),
					new ConnectionDetails(accessToken.getValue(), accessToken.getSecret(), ""));

			TwitterOperations twitter = twitterProvider.getTwitterApi(account.getId());
			twitterProvider.saveProviderAccountId(account.getId(), twitter.getScreenName());

			if (tweetIt) {
				try {
					twitter.tweet("Join me at the Greenhouse! " + account.getProfileUrl());
				} catch (DuplicateTweetException noWorries) {
				}
			}

			FlashMap.setSuccessMessage("Your Greenhouse account is now connected to your Twitter account!");
			return "redirect:/connect/twitter";
		}

		return "connect/twitterConnect";
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public String disconnectTwitter(Account account, HttpServletRequest request, Authentication authentication) {
		twitterProvider.disconnect(account.getId());
		return "redirect:/connect/twitter";
	}

	private String getCallbackUrl(boolean tweetIt) {
		// TODO : Replace the hard-coded localhost URL
		return "http://localhost:8080/greenhouse/connect/twitter" + (tweetIt ? "?tweetIt=on" : "");
	}
}
