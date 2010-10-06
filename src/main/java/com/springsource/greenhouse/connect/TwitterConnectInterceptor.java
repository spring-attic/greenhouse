package com.springsource.greenhouse.connect;

import org.springframework.social.twitter.DuplicateTweetException;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

import com.springsource.greenhouse.account.Account;

@Component
public class TwitterConnectInterceptor implements ConnectInterceptor<TwitterOperations> {

	private static final String POST_TWEET_PARAMETER = "postTweet";
	private static final String POST_TWEET_ATTRIBUTE = "twitterConnect." + POST_TWEET_PARAMETER;

	public void preConnect(AccountProvider<TwitterOperations> provider, WebRequest request) {
		if (StringUtils.hasText(request.getParameter(POST_TWEET_PARAMETER))) {
			request.setAttribute(POST_TWEET_ATTRIBUTE, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		}
	}

	public void postConnect(AccountProvider<TwitterOperations> provider, Account account, WebRequest request) {
		if (request.getAttribute(POST_TWEET_ATTRIBUTE, WebRequest.SCOPE_SESSION) != null) {
			try {
				provider.getApi(account.getId()).updateStatus("Join me at the Greenhouse! " + account.getProfileUrl());
			} catch (DuplicateTweetException e) {
			}
			request.removeAttribute(POST_TWEET_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		}
	}
}