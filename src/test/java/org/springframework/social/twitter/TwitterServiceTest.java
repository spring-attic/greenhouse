package org.springframework.social.twitter;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.social.twitter.TwitterService.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth.common.signature.SharedConsumerSecret;
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails;
import org.springframework.security.oauth.consumer.InMemoryProtectedResourceDetailsService;
import org.springframework.security.oauth.consumer.OAuthConsumerSupport;
import org.springframework.security.oauth.consumer.ProtectedResourceDetails;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.social.twitter.TwitterService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import edu.emory.mathcs.backport.java.util.Collections;

public class TwitterServiceTest {
	
	@Test
	@SuppressWarnings("unchecked")
	public void shouldSendUpdateMessageToTwitter() {
		OAuthConsumerToken accessToken = setupAccessToken();
		OAuthConsumerSupport oauthSupport = mockOAuthConsumerSupport(accessToken);		
		InMemoryProtectedResourceDetailsService resourceDetailsService = setupResourceDetailsService();
		TwitterService twitter = new TwitterService(oauthSupport, resourceDetailsService);		
		RestTemplate restTemplate = mock(RestTemplate.class);
		ResponseEntity<Map> responseEntity = new ResponseEntity<Map>(Collections.emptyMap(), HttpStatus.OK);
		when(restTemplate.exchange(eq(UPDATE_STATUS_URL), eq(HttpMethod.POST), any(HttpEntity.class), any(Class.class), any(Map.class))).thenReturn(responseEntity);		
		twitter.setRestTemplate(restTemplate);
		twitter.updateStatus(accessToken, "This is a test");
		HttpHeaders headers = setupHttpHeaders();
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.add("status", "This is a test");
		HttpEntity<MultiValueMap<String, String>> statusEntity = 
		    new HttpEntity<MultiValueMap<String, String>>(form, headers);
		verify(restTemplate).exchange(eq(TwitterService.UPDATE_STATUS_URL), eq(HttpMethod.POST), refEq(statusEntity), 
				eq(Map.class), any(Map.class)); 
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void shouldRetrieveScreenNameOfCurrentUser() {
		OAuthConsumerToken accessToken = setupAccessToken();
		OAuthConsumerSupport oauthSupport = mockOAuthConsumerSupport(accessToken);		
		InMemoryProtectedResourceDetailsService resourceDetailsService = setupResourceDetailsService();
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("screen_name", "s2greenhouse");
		ResponseEntity<Map> responseEntity = new ResponseEntity<Map>(responseMap , HttpStatus.OK);
		RestTemplate restTemplate = mock(RestTemplate.class);
		when(restTemplate.exchange(eq(VERIFY_CREDENTIALS_URL), eq(HttpMethod.GET), any(HttpEntity.class), any(Class.class), any(Map.class))).thenReturn(responseEntity);
		TwitterService twitter = new TwitterService(oauthSupport, resourceDetailsService);		
		twitter.setRestTemplate(restTemplate);
		
		String screenName = twitter.getScreenName(accessToken);
		Assert.assertEquals("s2greenhouse", screenName);
	}

	private HttpHeaders setupHttpHeaders() {
	    HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "OAuth_Header");
		headers.add("Content-Type", "application/x-www-form-urlencoded");
	    return headers;
    }

	private InMemoryProtectedResourceDetailsService setupResourceDetailsService() {
	    InMemoryProtectedResourceDetailsService resourceDetailsService = new InMemoryProtectedResourceDetailsService();
		Map<String, ProtectedResourceDetails> detailsStore = new HashMap<String, ProtectedResourceDetails>();
		BaseProtectedResourceDetails twitterDetails = new BaseProtectedResourceDetails();
		twitterDetails.setConsumerKey("twitterKey");
		twitterDetails.setSharedSecret(new SharedConsumerSecret("twitterSecret"));		
		detailsStore.put("twitter", twitterDetails);
		resourceDetailsService.setResourceDetailsStore(detailsStore);
	    return resourceDetailsService;
    }

	private OAuthConsumerSupport mockOAuthConsumerSupport(OAuthConsumerToken accessToken) {
	    OAuthConsumerSupport oauthSupport = mock(OAuthConsumerSupport.class);
		when(oauthSupport.getAuthorizationHeader(any(ProtectedResourceDetails.class), eq(accessToken), any(URL.class), 
				eq("POST"), any(Map.class))).thenReturn("OAuth_Header");
	    return oauthSupport;
    }

	private OAuthConsumerToken setupAccessToken() {
	    OAuthConsumerToken accessToken = new OAuthConsumerToken();
		accessToken.setAccessToken(true);
		accessToken.setResourceId("twitter");
		accessToken.setSecret("twitterSecret");
		accessToken.setValue("twitterToken");
	    return accessToken;
    }	
}
