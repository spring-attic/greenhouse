package org.springframework.social.facebook;

import java.util.List;

public interface FacebookOperations {
	FacebookUserInfo getUserInfo(String accessToken);
	List<String> getFriendIds(String accessToken);
	void postToWall(String accessToken, String message);
	void postToWall(String accessToken, String message, FacebookLink link);
}
