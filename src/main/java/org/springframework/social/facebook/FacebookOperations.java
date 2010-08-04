package org.springframework.social.facebook;

import java.util.List;

import org.springframework.util.MultiValueMap;

public interface FacebookOperations {
	FacebookUserInfo getUserInfo(String accessToken);
	List<String> getFriendIds(String accessToken);
	void postToWall(String accessToken, String message);
	void postToWall(String accessToken, String message, FacebookLink link);
	void publish(String accessToken, String object, String connection, MultiValueMap<String, String> data);
}
