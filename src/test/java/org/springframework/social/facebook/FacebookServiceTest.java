package org.springframework.social.facebook;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.social.facebook.FacebookService.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class FacebookServiceTest {
	
	private RestTemplate restTemplate;
	private FacebookUserInfo facebookUser;
	private FacebookService facebook;

	@Before
	public void setup() throws Exception {
		restTemplate = mock(RestTemplate.class);		
		facebookUser = new FacebookUserInfo();
		when(restTemplate.getForObject(GET_CURRENT_USER_INFO, FacebookUserInfo.class, "testToken")).
			thenReturn(facebookUser);		
		Map<String, List<Map<String, String>>> facebookFriendsMap = buildFriendsMap();
		when(restTemplate.getForObject(GET_CURRENT_USER_FRIENDS, Map.class, "testToken")).
			thenReturn(facebookFriendsMap);
		
		facebook = new FacebookService();
		facebook.setRestTemplate(restTemplate);				
	}

	@Test
	public void shouldGetUserInformation() {
		assertSame(facebookUser, facebook.getUserInfo("testToken"));
	}
	
	@Test
	public void shouldGetUserFriends() {
		List<String> friendIds = facebook.getFriendIds("testToken");
		assertEquals(Arrays.asList("123", "456", "789"), friendIds);
	}
	
	@Test
	public void shouldPostMessageToWall() {
		facebook.postToWall("testToken", "This is a test");		
		MultiValueMap<String, String> messageMap = new LinkedMultiValueMap<String, String>();
		messageMap.set("access_token", "testToken");
		messageMap.set("message", "This is a test");
		verify(restTemplate).postForLocation(eq(USER_FEED_URL), eq(messageMap));
	}

	@Test
	public void shouldPostLinkToWall() {
		facebook.postToWall("testToken", "This is a test", new FacebookLink("link", "name", "caption", "description"));		
		MultiValueMap<String, String> messageMap = new LinkedMultiValueMap<String, String>();
		messageMap.set("access_token", "testToken");
		messageMap.set("message", "This is a test");
		messageMap.set("link", "link");
		messageMap.set("name", "name");
		messageMap.set("caption", "caption");
		messageMap.set("description", "description");
		verify(restTemplate).postForLocation(eq(USER_FEED_URL), eq(messageMap));
	}

	// helper method
	private Map<String, List<Map<String, String>>> buildFriendsMap() {
	    Map<String, List<Map<String, String>>> facebookFriendsMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String, String>> friendList = new ArrayList<Map<String,String>>();
		friendList.add(Collections.singletonMap("id", "123"));
		friendList.add(Collections.singletonMap("id", "456"));
		friendList.add(Collections.singletonMap("id", "789"));
		facebookFriendsMap.put("data", friendList);
	    return facebookFriendsMap;
    }
}
