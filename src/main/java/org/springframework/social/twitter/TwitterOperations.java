package org.springframework.social.twitter;

import java.util.List;

import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;

public interface TwitterOperations {

	String getScreenName(OAuthConsumerToken accessToken);
	List<String> getFriends(OAuthConsumerToken accessToken, String screenName);
	void updateStatus(OAuthConsumerToken accessToken, String message);
	List<Tweet> getTweetsForTag(OAuthConsumerToken accessToken, String tag);
	List<Tweet> getTweetsForTag(OAuthConsumerToken accessToken, String tag, int resultsPerPage, int page);
}