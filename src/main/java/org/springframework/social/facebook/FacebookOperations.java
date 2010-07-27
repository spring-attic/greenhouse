package org.springframework.social.facebook;

import java.util.List;

public interface FacebookOperations {
	FacebookUserInfo getUserInfo(String accessToken);
	List<String> getFriendIds(String accessToken);
}
