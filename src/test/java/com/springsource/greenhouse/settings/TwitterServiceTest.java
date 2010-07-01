package com.springsource.greenhouse.settings;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth.common.signature.SharedConsumerSecret;
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails;
import org.springframework.security.oauth.consumer.InMemoryProtectedResourceDetailsService;
import org.springframework.security.oauth.consumer.OAuthConsumerSupport;
import org.springframework.security.oauth.consumer.ProtectedResourceDetails;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class TwitterServiceTest {
	@Test
	@SuppressWarnings("unchecked")
	public void shouldSendUpdateMessageToTwitter() {
		OAuthConsumerToken accessToken = new OAuthConsumerToken();
		accessToken.setAccessToken(true);
		accessToken.setResourceId("twitter");
		accessToken.setSecret("twitterSecret");
		accessToken.setValue("twitterToken");

		OAuthConsumerSupport oauthSupport = mock(OAuthConsumerSupport.class);
		when(oauthSupport.getAuthorizationHeader(any(ProtectedResourceDetails.class), eq(accessToken), any(URL.class), 
				eq("POST"), any(Map.class))).thenReturn("OAuth_Header");
		
		InMemoryProtectedResourceDetailsService resourceDetailsService = new InMemoryProtectedResourceDetailsService();
		Map<String, ProtectedResourceDetails> detailsStore = new HashMap<String, ProtectedResourceDetails>();
		BaseProtectedResourceDetails twitterDetails = new BaseProtectedResourceDetails();
		twitterDetails.setConsumerKey("twitterKey");
		twitterDetails.setSharedSecret(new SharedConsumerSecret("twitterSecret"));		
		detailsStore.put("twitter", twitterDetails);
		resourceDetailsService.setResourceDetailsStore(detailsStore);
		TwitterService twitter = new TwitterService(oauthSupport, resourceDetailsService);		
		RestTemplate restTemplate = mock(RestTemplate.class);
		twitter.setRestTemplate(restTemplate);
		
		twitter.updateStatus(accessToken, "This is a test");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "OAuth_Header");
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.add("status", "This is a test");
		
		HttpEntity<MultiValueMap<String, String>> statusEntity = 
		    new HttpEntity<MultiValueMap<String, String>>(form, headers);

		verify(restTemplate).exchange(eq(TwitterService.UPDATE_STATUS_URL), eq(HttpMethod.POST), refEq(statusEntity), 
				eq(Map.class), any(Map.class)); 
	}
}
