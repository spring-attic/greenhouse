package com.springsource.greenhouse.connect;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.social.linkedin.LinkedInOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;

@Controller
@RequestMapping("/connect/linkedin")
public class LinkedInConnectController {
	
	private static final String OAUTH_TOKEN_ATTRIBUTE = "oauthToken";

	private final AccountProvider<LinkedInOperations> accountProvider;

	private final String callbackUrl;
	
	@Inject
	public LinkedInConnectController(@Named("linkedInAccountProvider") AccountProvider<LinkedInOperations> accountProvider, @Value("${application.secureUrl}") String applicationUrl) {
		this.accountProvider = accountProvider;
		this.callbackUrl = applicationUrl + "/connect/linkedin";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String linkedInConnect(Account account) {
		if (accountProvider.isConnected(account.getId())) {
			return "connect/linkedInConnected";
		} else {
			return "connect/linkedInConnect";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String connect(WebRequest request) {
		OAuthToken requestToken = accountProvider.getRequestToken(callbackUrl);		
		request.setAttribute(OAUTH_TOKEN_ATTRIBUTE, requestTokenHolder(requestToken), WebRequest.SCOPE_SESSION);
		return "redirect:" + accountProvider.getAuthorizeUrl(requestToken.getValue());
	}

	@RequestMapping(method = RequestMethod.GET, params = "oauth_token")
	public String authorizeCallback(@RequestParam("oauth_token") String token, @RequestParam("oauth_verifier") String verifier, Account account, WebRequest request) {
		@SuppressWarnings("unchecked")
		Map<String, Object> requestTokenHolder = (Map<String, Object>) request.getAttribute(OAUTH_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		if (requestTokenHolder == null) {
			return "connect/linkedInConnect";
		}
		request.removeAttribute(OAUTH_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		OAuthToken requestToken = (OAuthToken) requestTokenHolder.get("value");
		LinkedInOperations linkedIn = accountProvider.connect(account.getId(), requestToken, verifier);
		accountProvider.updateProviderAccountId(account.getId(), linkedIn.getUserInfo().getId());
		FlashMap.setSuccessMessage("Your Greenhouse account is now connected to your LinkedIn account!");
		return "redirect:/connect/linkedin";
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public String disconnectLinkedIn(Account account, HttpServletRequest request, Authentication authentication) {
		accountProvider.disconnect(account.getId());
		return "redirect:/connect/linkedin";
	}

	// internal helpers

	private Map<String, Object> requestTokenHolder(OAuthToken requestToken) {
		Map<String, Object> holder = new HashMap<String, Object>(2, 1);
		holder.put("value", requestToken);
		return holder;
	}
}
