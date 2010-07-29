package org.springframework.social.facebook;

import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class FacebookService implements FacebookOperations {
	private RestTemplate restTemplate;
	
	public FacebookService() {
		MappingJacksonHttpMessageConverter jsonMessageConverter = new MappingJacksonHttpMessageConverter();
		jsonMessageConverter.setSupportedMediaTypes(asList(new MediaType("text", "javascript")) );
		this.restTemplate = new RestTemplate();
		this.restTemplate.getMessageConverters().add(jsonMessageConverter);
	}

	public FacebookUserInfo getUserInfo(String accessToken) {
		return restTemplate.getForObject(GET_CURRENT_USER_INFO, FacebookUserInfo.class, accessToken);
    }

	public List<String> getFriendIds(String accessToken) {
		Map<String, List<Map<String, String>>> results = 
			restTemplate.getForObject(GET_CURRENT_USER_FRIENDS, Map.class, accessToken);		
		List<Map<String, String>> friends = results.get("data");
		
		List<String> friendIds = new ArrayList<String>();
		for (Map<String, String> friendData : friends) {
	        friendIds.add(friendData.get("id"));
        }
	    return friendIds;
    }
	
	public void postToWall(String accessToken, String message) {
		MultiValueMap<String, String> map = createBaseWallMessage(accessToken, message);
		restTemplate.postForLocation(USER_FEED_URL, map);
	}
	
	public void postToWall(String accessToken, String message, FacebookLink link) {
		MultiValueMap<String, String> map = createBaseWallMessage(accessToken, message);
		map.set("link", link.getLink());
		map.set("name", link.getName());
		map.set("caption", link.getCaption());
		map.set("description", link.getDescription());		
		restTemplate.postForLocation(USER_FEED_URL, map);
	}

	private MultiValueMap<String, String> createBaseWallMessage(String accessToken, String message) {
	    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>(); 
		map.set("access_token", accessToken);
		map.set("message", message);
	    return map;
    }
	
	// to support unit testing
	void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	static final String GET_CURRENT_USER_FRIENDS = "https://graph.facebook.com/me/friends?access_token={token}";
	static final String GET_CURRENT_USER_INFO = "https://graph.facebook.com/me?access_token={token}";
	static final String USER_FEED_URL = "https://graph.facebook.com/me/feed";
}
