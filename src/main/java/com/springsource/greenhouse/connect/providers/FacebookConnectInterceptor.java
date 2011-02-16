package com.springsource.greenhouse.connect.providers;

import org.springframework.social.connect.ServiceProvider;
import org.springframework.social.connect.ServiceProviderConnection;
import org.springframework.social.facebook.FacebookApi;
import org.springframework.social.facebook.FacebookLink;
import org.springframework.social.twitter.DuplicateTweetException;
import org.springframework.social.web.connect.ConnectInterceptor;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

import com.springsource.greenhouse.account.Account;

public class FacebookConnectInterceptor implements ConnectInterceptor<FacebookApi> {

	@Override
	public void preConnect(ServiceProvider<FacebookApi> provider, WebRequest request) {
		if (StringUtils.hasText(request.getParameter(POST_TO_WALL_PARAMETER))) {
			request.setAttribute(POST_TO_WALL_ATTRIBUTE, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		}
	}

	@Override
	public void postConnect(ServiceProvider<FacebookApi> provider, ServiceProviderConnection<FacebookApi> connection,
			WebRequest request) {
		try {
			// relies on AccountExposingHandlerInterceptor to have put the account in the request.
			Account account = (Account) request.getAttribute("account", WebRequest.SCOPE_REQUEST);
			connection.getServiceApi().updateStatus("Join me at the Greenhouse!", new FacebookLink(account.getProfileUrl(), "Greenhouse", "Where Spring developers hang out.",
				"We help you connect with fellow application developers and take advantage of everything the Spring community has to offer."));
		} catch (DuplicateTweetException e) {
		}
		request.removeAttribute(POST_TO_WALL_ATTRIBUTE, WebRequest.SCOPE_SESSION);
	}

	private static final String POST_TO_WALL_PARAMETER = "postToWall";

	private static final String POST_TO_WALL_ATTRIBUTE = "facebookConnect." + POST_TO_WALL_PARAMETER;

}
