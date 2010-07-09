package org.springframework.social.twitter;

import java.util.List;

import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;

public interface TwitterOperations {

	String getScreenName(OAuthConsumerToken accessToken);
	List<String> getFriends(OAuthConsumerToken accessToken, String screenName);
	void updateStatus(OAuthConsumerToken accessToken, String message);
	SearchResults search(OAuthConsumerToken accessToken, String query);
	SearchResults search(OAuthConsumerToken accessToken, String query, int page, int resultsPerPage, int sinceId, int maxId);
}