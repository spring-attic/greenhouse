package com.springsource.greenhouse.connect;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.GenericTypeResolver;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
	private MultiValueMap<Class<?>, ConnectInterceptor<?>> interceptors;

	@Inject
	public ConnectController(AccountProviderFactory providerFactory,
			@Value("${application.secureUrl}") String applicationUrl) {
		this.providerFactory = providerFactory;
		this.baseCallbackUrl = applicationUrl + "/connect/";
		this.interceptors = new LinkedMultiValueMap<Class<?>, ConnectInterceptor<?>>();
	}

	@Autowired(required = false)
	public void setInterceptors(List<ConnectInterceptor<?>> interceptors) {
		for (ConnectInterceptor<?> interceptor : interceptors) {
			this.interceptors.add(
					GenericTypeResolver.resolveTypeArgument(interceptor.getClass(), ConnectInterceptor.class),
					interceptor);
		}
	}

	@RequestMapping(value = "/{provider}", method = RequestMethod.GET)
	public String connect(@PathVariable("provider") String provider, Account account) {
		String basePath = "connect/" + provider;
		if (getAccountProvider(provider).isConnected(account.getId())) {
			return basePath + "Connected";
		} else {
			return basePath + "Connect";
		}
	}

	@RequestMapping(value = "/{provider}", method = RequestMethod.POST)
	public String connect(@PathVariable("provider") String provider, WebRequest request) {
		AccountProvider<?> accountProvider = getAccountProvider(provider);
		preConnect(accountProvider, request);
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
		Map<String, Object> requestTokenHolder = (Map<String, Object>) request.getAttribute(OAUTH_TOKEN_ATTRIBUTE,
				WebRequest.SCOPE_SESSION);
		OAuthToken requestToken = (OAuthToken) requestTokenHolder.get("value");
		if (requestToken == null) {
			return "connect/" + provider + "Connect";
		}

		request.removeAttribute(OAUTH_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		AccountProvider<?> accountProvider = getAccountProvider(provider);
		accountProvider.connect(account.getId(), requestToken, verifier);
		postConnect(accountProvider, account, request);
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

	@SuppressWarnings("unchecked")
	private void preConnect(AccountProvider<?> provider, WebRequest request) {
		List<ConnectInterceptor<?>> matchingInterceptors = interceptorsForProvider(provider);
		for (ConnectInterceptor interceptor : matchingInterceptors) {
			interceptor.preConnect(provider, request);
		}
	}

	@SuppressWarnings("unchecked")
	private void postConnect(AccountProvider<?> provider, Account account, WebRequest request) {
		List<ConnectInterceptor<?>> matchingInterceptors = interceptorsForProvider(provider);
		for (ConnectInterceptor interceptor : matchingInterceptors) {
			interceptor.postConnect(provider, account, request);
		}
	}

	private List<ConnectInterceptor<?>> interceptorsForProvider(AccountProvider<?> provider) {
		Class<?> providerApiType = GenericTypeResolver.resolveTypeArgument(provider.getClass(), AccountProvider.class);
		List<ConnectInterceptor<?>> typedInterceptors = interceptors.get(providerApiType);
		if (typedInterceptors == null) {
			typedInterceptors = Collections.emptyList();
		}
		return typedInterceptors;
	}

}
