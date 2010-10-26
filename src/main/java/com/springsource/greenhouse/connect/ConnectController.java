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
	
	private final ServiceProviderFactory serviceProviderFactory;
	
	private final String baseCallbackUrl;
	
	private MultiValueMap<Class<?>, ConnectInterceptor<?>> interceptors;

	@Inject
	public ConnectController(ServiceProviderFactory serviceProviderFactory, @Value("${application.secureUrl}") String applicationUrl) {
		this.serviceProviderFactory = serviceProviderFactory;
		this.baseCallbackUrl = applicationUrl + "/connect/";
		this.interceptors = new LinkedMultiValueMap<Class<?>, ConnectInterceptor<?>>();
	}

	public void setInterceptors(List<ConnectInterceptor<?>> interceptors) {
		for (ConnectInterceptor<?> interceptor : interceptors) {
			Class<?> providerType = GenericTypeResolver.resolveTypeArgument(interceptor.getClass(),  ConnectInterceptor.class);
			this.interceptors.add(providerType, interceptor);
		}
	}

	@RequestMapping(value="/{name}", method=RequestMethod.GET)
	public String connect(Account account, @PathVariable String name) {
		String baseViewPath = "connect/" + name;
		if (getServiceProvider(name).isConnected(account.getId())) {
			return baseViewPath + "Connected";
		} else {
			return baseViewPath + "Connect";
		}
	}

	@RequestMapping(value="/{name}", method=RequestMethod.POST)
	public String connect(@PathVariable String name, WebRequest request) {
		ServiceProvider<?> provider = getServiceProvider(name);
		preConnect(provider, request);
		OAuthToken requestToken = provider.fetchNewRequestToken(baseCallbackUrl + name);
		request.setAttribute(OAUTH_TOKEN_ATTRIBUTE, requestToken, WebRequest.SCOPE_SESSION);
		return "redirect:" + provider.buildAuthorizeUrl(requestToken.getValue());
	}
	
	@RequestMapping(value="/{name}", method=RequestMethod.GET, params="oauth_token")
	public String authorizeCallback(@PathVariable String name, @RequestParam("oauth_token") String token,
			@RequestParam(value="oauth_verifier", defaultValue="verifier") String verifier, Account account, WebRequest request) {
		OAuthToken requestToken = (OAuthToken) request.getAttribute(OAUTH_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		if (requestToken == null) {
			return "connect/" + name + "Connect";
		}
		request.removeAttribute(OAUTH_TOKEN_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		ServiceProvider<?> provider = getServiceProvider(name);
		provider.connect(account.getId(), requestToken, verifier);
		postConnect(provider, account, request);
		FlashMap.setSuccessMessage("Your Greenhouse account is now connected to your " + provider.getDisplayName() + " account!");
		return "redirect:/connect/" + name;
	}

	@RequestMapping(value="/{name}", method=RequestMethod.DELETE)
	public String disconnect(@PathVariable String name, Account account) {
		getServiceProvider(name).disconnect(account.getId());
		return "redirect:/connect/" + name;
	}

	// internal helpers

	private ServiceProvider<?> getServiceProvider(String name) {
		return serviceProviderFactory.getServiceProvider(name);
	}

	@SuppressWarnings("unchecked")
	private void preConnect(ServiceProvider<?> provider, WebRequest request) {
		for (ConnectInterceptor interceptor : interceptingConnectionsTo(provider)) {
			interceptor.preConnect(provider, request);
		}
	}

	@SuppressWarnings("unchecked")
	private void postConnect(ServiceProvider<?> provider, Account account, WebRequest request) {
		for (ConnectInterceptor interceptor : interceptingConnectionsTo(provider)) {
			interceptor.postConnect(provider, account, request);
		}
	}

	private List<ConnectInterceptor<?>> interceptingConnectionsTo(ServiceProvider<?> provider) {
		Class<?> serviceType = GenericTypeResolver.resolveTypeArgument(provider.getClass(), ServiceProvider.class);
		List<ConnectInterceptor<?>> typedInterceptors = interceptors.get(serviceType);
		if (typedInterceptors == null) {
			typedInterceptors = Collections.emptyList();
		}
		return typedInterceptors;
	}
	
	private static final String OAUTH_TOKEN_ATTRIBUTE = "oauthToken";

}