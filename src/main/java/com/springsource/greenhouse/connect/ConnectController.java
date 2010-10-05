package com.springsource.greenhouse.connect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.social.core.SocialProviderOperations;
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
	private final String baseCallbackUrl;
	private List<ConnectInterceptor> interceptors;

	@Inject
	public ConnectController(AccountProviderFactory providerFactory,
			@Value("${application.secureUrl}") String applicationUrl) {
		this.providerFactory = providerFactory;
		this.baseCallbackUrl = applicationUrl + "/connect/";
	}

	@Autowired(required = false)
	public void setInterceptors(List<ConnectInterceptor> interceptors) {
		this.interceptors = interceptors;
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
		preConnect(provider, request);
		AccountProvider<?> accountProvider = getAccountProvider(provider);
		OAuthToken requestToken = accountProvider.getRequestToken(baseCallbackUrl + provider);
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

		// TODO: Leaning on SocialProviderOperations like this will make it
		// tricky to make an account provider that is based on something like
		// Twitter4J
		SocialProviderOperations api = (SocialProviderOperations) accountProvider.connect(account.getId(),
				requestToken, verifier);
		accountProvider.updateProviderAccountId(account.getId(), api.getProfileId());
		postConnect(provider, request, api, account);
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

	private void preConnect(String providerName, WebRequest request) {
		if (interceptors == null)
			return;

		for (ConnectInterceptor interceptor : interceptors) {
			if (interceptor.supportsProvider(providerName)) {
				interceptor.preConnect(request);
			}
		}
	}

	private void postConnect(String providerName, WebRequest request, SocialProviderOperations api, Account account) {
		if (interceptors == null)
			return;

		for (ConnectInterceptor interceptor : interceptors) {
			if (interceptor.supportsProvider(providerName)) {
				interceptor.postConnect(request, api, account);
			}
		}
	}
}
