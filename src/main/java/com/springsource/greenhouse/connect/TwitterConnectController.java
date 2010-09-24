package com.springsource.greenhouse.connect;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
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
	public TwitterConnectController(@Named("twitterAccountProvider") AccountProvider<TwitterOperations> accountProvider) {
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
	public String connect(@RequestParam(required=false, defaultValue="false") boolean postTweet, WebRequest request) {
		OAuthToken requestToken = accountProvider.getRequestToken();
		request.setAttribute(OAUTH_TOKEN_ATTRIBUTE, requestTokenHolder(requestToken, postTweet), WebRequest.SCOPE_SESSION);
		return "redirect:" + accountProvider.getAuthorizeUrl(requestToken.getValue());
	}

	@RequestMapping(method=RequestMethod.GET, params="oauth_token")
	public String authorizeCallback(@RequestParam("oauth_token") String token, @RequestParam("oauth_verifier") String verifier, Account account, WebRequest request) {
		@SuppressWarnings("unchecked")
		Map<String, Object> requestTokenHolder = (Map<String, Object>) request.getAttribute(OAUTH_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		if (requestTokenHolder == null) {
			return "connect/twitterConnect";
		}
		request.removeAttribute(OAUTH_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		OAuthToken requestToken = (OAuthToken) requestTokenHolder.get("value");		
		TwitterOperations twitter = accountProvider.connect(account.getId(), requestToken, verifier);
		accountProvider.updateProviderAccountId(account.getId(), twitter.getScreenName());
		if (requestTokenHolder.containsKey("postTweet")) {
			twitter.tweet("Join me at the Greenhouse! " + account.getProfileUrl());
		}
		FlashMap.setSuccessMessage("Your Greenhouse account is now connected to your Twitter account!");
		return "redirect:/connect/twitter";
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public String disconnectTwitter(Account account, HttpServletRequest request, Authentication authentication) {
		accountProvider.disconnect(account.getId());
		return "redirect:/connect/twitter";
	}
	
	// internal helpers
	
	private Map<String, Object> requestTokenHolder(OAuthToken requestToken, boolean postTweet) {
		Map<String, Object> holder = new HashMap<String, Object>(2, 1);
		holder.put("value", requestToken);
		if (postTweet) {
			holder.put("postTweet", Boolean.TRUE);
		}
		return holder;
	}

}