package com.springsource.greenhouse.connect;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.social.core.SocialOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;

@Controller
@RequestMapping("/connect")
public class ConnectController {
	private static final String OAUTH_TOKEN_ATTRIBUTE = "oauthToken";
	private final AccountProviderFactory providerFactory;

	@Inject
	public ConnectController(AccountProviderFactory providerFactory) {
		this.providerFactory = providerFactory;
	}

	@RequestMapping(value = "/{provider}", method = RequestMethod.GET)
	public String connect(@PathVariable("provider") String provider, Account account) {
		if (getAccountProvider(provider).isConnected(account.getId())) {
			return "connect/" + provider + "Connected";
		} else {
			return "connect/" + provider + "Connect";
		}
	}

	@RequestMapping(value = "/{provider}", method = RequestMethod.POST)
	public String connect(@PathVariable("provider") String provider, WebRequest request) {
		AccountProvider<?> accountProvider = getAccountProvider(provider);
		OAuthToken requestToken = accountProvider.getRequestToken();
		request.setAttribute(OAUTH_TOKEN_ATTRIBUTE, requestTokenHolder(requestToken),
				WebRequest.SCOPE_SESSION);
		return "redirect:" + accountProvider.getAuthorizeUrl(requestToken.getValue());
	}

	@RequestMapping(value = "/{provider}", method = RequestMethod.GET, params = "oauth_token")
	public String authorizeCallback(@PathVariable("provider") String provider,
			@RequestParam("oauth_token") String token,
			@RequestParam(value = "oauth_verifier", defaultValue = "verifier") String verifier, Account account,
			WebRequest request) {
		@SuppressWarnings("unchecked")
		Map<String, Object> requestTokenHolder = (Map<String, Object>) request.getAttribute(OAUTH_TOKEN_ATTRIBUTE,
				WebRequest.SCOPE_SESSION);
		if (requestTokenHolder == null) {
			return "connect/" + provider + "Connect";
		}
		request.removeAttribute(OAUTH_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		OAuthToken requestToken = (OAuthToken) requestTokenHolder.get("value");
		AccountProvider<?> accountProvider = getAccountProvider(provider);

		SocialOperations api = (SocialOperations) accountProvider.connect(account.getId(), requestToken, verifier);
		accountProvider.updateProviderAccountId(account.getId(), api.getProfileId());
		FlashMap.setSuccessMessage("Your Greenhouse account is now connected to your "
				+ accountProvider.getDisplayName() + " account!");
		return "redirect:/connect/" + provider;
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public String disconnect(@PathVariable("provider") String provider, Account account,
			HttpServletRequest request, Authentication authentication) {
		getAccountProvider(provider).disconnect(account.getId());
		return "redirect:/connect/" + provider;
	}

	// internal helpers

	private AccountProvider<?> getAccountProvider(String name) {
		return providerFactory.getAccountProviderByName(name);
	}

	private Map<String, Object> requestTokenHolder(OAuthToken requestToken) {
		Map<String, Object> holder = new HashMap<String, Object>(2, 1);
		holder.put("value", requestToken);
		return holder;
	}
}
