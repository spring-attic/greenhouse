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
		return restTemplate.getForObject(OBJECT_URL + ACCESS_TOKEN_PARAM, FacebookUserInfo.class, "me", accessToken);
    }

	public List<String> getFriendIds(String accessToken) {
		Map<String, List<Map<String, String>>> results = 
			restTemplate.getForObject(CONNECTION_URL + ACCESS_TOKEN_PARAM, Map.class, 
					CURRENT_USER, FRIENDS, accessToken);		
		List<Map<String, String>> friends = results.get("data");
		
		List<String> friendIds = new ArrayList<String>();
		for (Map<String, String> friendData : friends) {
	        friendIds.add(friendData.get("id"));
        }
	    return friendIds;
    }
	
	public void postToWall(String accessToken, String message) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.set("message", message);
		publish(accessToken, CURRENT_USER, FEED, map);
	}
	
	public void postToWall(String accessToken, String message, FacebookLink link) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.set("link", link.getLink());
		map.set("name", link.getName());
		map.set("caption", link.getCaption());
		map.set("description", link.getDescription());
		map.set("message", message);
		publish(accessToken, CURRENT_USER, FEED, map);
	}
	
	public void publish(String accessToken, String object, String connection, MultiValueMap<String, String> data) {
		MultiValueMap<String, String> requestData = new LinkedMultiValueMap<String, String>(data);
		requestData.add("access_token", accessToken);
		restTemplate.postForLocation(CONNECTION_URL, requestData, object, connection);
	}
	
	// to support unit testing
	void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	static final String OBJECT_URL = "https://graph.facebook.com/{objectId}";
	static final String CONNECTION_URL = OBJECT_URL + "/{connection}";
	
	static final String ACCESS_TOKEN_PARAM = "?access_token={token}";
	static final String FRIENDS = "friends";
	static final String FEED = "feed";
	static final String CURRENT_USER = "me";
}
