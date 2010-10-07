package com.springsource.greenhouse.connect;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.GenericTypeResolver;
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
	
	private final AccountProviderFactory providerFactory;
	
	private final String baseCallbackUrl;
	
	private MultiValueMap<Class<?>, ConnectInterceptor<?>> interceptors;

	@Inject
	public ConnectController(AccountProviderFactory providerFactory, @Value("${application.secureUrl}") String applicationUrl) {
		this.providerFactory = providerFactory;
		this.baseCallbackUrl = applicationUrl + "/connect/";
		this.interceptors = new LinkedMultiValueMap<Class<?>, ConnectInterceptor<?>>();
	}

	public void setInterceptors(List<ConnectInterceptor<?>> interceptors) {
		for (ConnectInterceptor<?> interceptor : interceptors) {
			Class<?> providerType = GenericTypeResolver.resolveTypeArgument(interceptor.getClass(),  ConnectInterceptor.class);
			this.interceptors.add(providerType, interceptor);
		}
	}

	@RequestMapping(value="/{provider}", method=RequestMethod.GET)
	public String connect(Account account, @PathVariable String provider) {
		String baseViewPath = "connect/" + provider;
		if (getAccountProvider(provider).isConnected(account.getId())) {
			return baseViewPath + "Connected";
		} else {
			return baseViewPath + "Connect";
		}
	}

	@RequestMapping(value="/{provider}", method=RequestMethod.POST)
	public String connect(@PathVariable String provider, WebRequest request) {
		AccountProvider<?> accountProvider = getAccountProvider(provider);
		preConnect(accountProvider, request);
		OAuthToken requestToken = accountProvider.fetchNewRequestToken(baseCallbackUrl + provider);
		request.setAttribute(OAUTH_TOKEN_ATTRIBUTE, requestToken, WebRequest.SCOPE_SESSION);
		return "redirect:" + accountProvider.buildAuthorizeUrl(requestToken.getValue());
	}
	
	@RequestMapping(value="/{provider}", method=RequestMethod.GET, params="oauth_token")
	public String authorizeCallback(@PathVariable String provider, @RequestParam("oauth_token") String token,
			@RequestParam(value="oauth_verifier", defaultValue="verifier") String verifier,
			Account account, WebRequest request) {
		OAuthToken requestToken = (OAuthToken) request.getAttribute(OAUTH_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		if (requestToken == null) {
			return "connect/" + provider + "Connect";
		}
		request.removeAttribute(OAUTH_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		AccountProvider<?> accountProvider = getAccountProvider(provider);
		accountProvider.connect(account.getId(), requestToken, verifier);
		postConnect(accountProvider, account, request);
		FlashMap.setSuccessMessage("Your Greenhouse account is now connected to your " + accountProvider.getDisplayName() + " account!");
		return "redirect:/connect/" + provider;
	}

	@RequestMapping(method=RequestMethod.DELETE)
	public String disconnect(Account account, @PathVariable String provider) {
		getAccountProvider(provider).disconnect(account.getId());
		return "redirect:/connect/" + provider;
	}

	// internal helpers

	private AccountProvider<?> getAccountProvider(String name) {
		return providerFactory.getAccountProviderByName(name);
	}

	private void preConnect(AccountProvider<?> provider, WebRequest request) {
		for (ConnectInterceptor interceptor : interceptingConnectionsTo(provider)) {
			interceptor.preConnect(provider, request);
		}
	}

	private void postConnect(AccountProvider<?> provider, Account account, WebRequest request) {
		for (ConnectInterceptor interceptor : interceptingConnectionsTo(provider)) {
			interceptor.postConnect(provider, account, request);
		}
	}

	private List<ConnectInterceptor<?>> interceptingConnectionsTo(AccountProvider<?> provider) {
		Class<?> providerApiType = GenericTypeResolver.resolveTypeArgument(provider.getClass(), AccountProvider.class);
		List<ConnectInterceptor<?>> typedInterceptors = interceptors.get(providerApiType);
		if (typedInterceptors == null) {
			typedInterceptors = Collections.emptyList();
		}
		return typedInterceptors;
	}
	
	private static final String OAUTH_TOKEN_ATTRIBUTE = "oauthToken";

}