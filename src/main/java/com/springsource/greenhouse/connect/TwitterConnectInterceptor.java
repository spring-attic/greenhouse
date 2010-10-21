package com.springsource.greenhouse.connect;

import org.springframework.social.twitter.DuplicateTweetException;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

import com.springsource.greenhouse.account.Account;

@Component
public class TwitterConnectInterceptor implements ConnectInterceptor<TwitterOperations> {

	private static final String POST_TWEET_PARAM = "twitterConnect.postTweet";

	public void preConnect(AccountProvider<TwitterOperations> provider, WebRequest request) {
		if (StringUtils.hasText(request.getParameter(POST_TWEET_PARAM))) {
			request.setAttribute(POST_TWEET_PARAM, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		}
	}

	public void postConnect(AccountProvider<TwitterOperations> provider, Account account, WebRequest request) {
		if (request.getAttribute(POST_TWEET_PARAM, WebRequest.SCOPE_SESSION) != null) {
			try {
				// TODO consider renaming setStatus to updateStatus
				provider.getApi(account.getId()).setStatus("Join me at the Greenhouse! " + account.getProfileUrl());
			} catch (DuplicateTweetException e) {
			}
			request.removeAttribute(POST_TWEET_PARAM, WebRequest.SCOPE_SESSION);
		}
	}
}