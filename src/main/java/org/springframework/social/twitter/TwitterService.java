package org.springframework.social.twitter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth.consumer.OAuthConsumerSupport;
import org.springframework.security.oauth.consumer.ProtectedResourceDetails;
import org.springframework.security.oauth.consumer.ProtectedResourceDetailsService;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class TwitterService {
	
	private OAuthConsumerSupport oauthSupport;
	
	private ProtectedResourceDetailsService resourceDetailsService;
	
	private RestTemplate restTemplate;

	public TwitterService(OAuthConsumerSupport oauthSupport, ProtectedResourceDetailsService resourceDetailsService) {
		this.oauthSupport = oauthSupport;
		this.resourceDetailsService = resourceDetailsService;
		this.restTemplate = new RestTemplate();
	}

	public String getScreenName(OAuthConsumerToken accessToken) {
		Map<String, String> parameters = Collections.emptyMap();
		Map<String, String> response = exchangeForMap(accessToken, HttpMethod.GET, VERIFY_CREDENTIALS_URL, parameters);
		return response.get("screen_name");
	}
	
	public List<String> getFriends(OAuthConsumerToken accessToken, String screenName) {
		Map<String, String> parameters = Collections.singletonMap("screen_name", screenName);
		List<Map<String, String>> response = exchangeForList(accessToken, HttpMethod.GET, FRIENDS_STATUSES_URL, parameters);
		List<String> friends = new ArrayList<String>(response.size());
		for(Map<String, String> item : response) {
			friends.add(item.get("screen_name"));
		}
		return friends;
	}

	public void updateStatus(OAuthConsumerToken accessToken, String message) {
		Map<String, String> parameters = Collections.singletonMap("status", message);
		// TODO should we just use post here?
		exchangeForMap(accessToken, HttpMethod.POST, UPDATE_STATUS_URL, parameters);
	}

	// internal helpers

	private Map exchangeForMap(OAuthConsumerToken accessToken, HttpMethod method, String url, Map<String, String> parameters) {
		return exchange(accessToken, method, url, parameters, Map.class);
	}

	private List exchangeForList(OAuthConsumerToken accessToken, HttpMethod method, String url, Map<String, String> parameters) {
		return exchange(accessToken, method, url, parameters, List.class);
	}

	private <T> T exchange(OAuthConsumerToken accessToken, HttpMethod method, String twitterUrl, Map<String, String> parameters, Class<T> responseType) {
		HttpHeaders headers = buildRequestHeaders(accessToken, method, twitterUrl, parameters);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		HashMap<String, Object> uriVariables = new HashMap<String, Object>();
		if (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT)) {
			for (String key : parameters.keySet()) {
				form.add(key, parameters.get(key));
			}
		} else {
			for (String key : parameters.keySet()) {
				uriVariables.put(key, parameters.get(key));
			}				
		}
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(form, headers);
		return restTemplate.exchange(twitterUrl, method, requestEntity, responseType, uriVariables).getBody();
	}

	private HttpHeaders buildRequestHeaders(OAuthConsumerToken accessToken, HttpMethod method, String twitterUrl, Map<String, String> parameters) {
	    try {
	    	ProtectedResourceDetails details = resourceDetailsService.loadProtectedResourceDetailsById("twitter");
	        String authorizationHeader = oauthSupport.getAuthorizationHeader(details, accessToken, new URL(twitterUrl), method.name(), parameters);
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Authorization", authorizationHeader);
	        headers.add("Content-Type", "application/x-www-form-urlencoded");
	        return headers;
        } catch (MalformedURLException e) {
        	throw new RestClientException("Malformed URL: " +twitterUrl, e);
        }
    }

	// to support unit testing

	void setRestTemplate(RestTemplate mock) {
		this.restTemplate = mock;
	}
	
	static final String VERIFY_CREDENTIALS_URL = "http://api.twitter.com/1/account/verify_credentials.json";
	static final String FRIENDS_STATUSES_URL = "http://api.twitter.com/1/statuses/friends.json?screen_name={screen_name}";
	static final String UPDATE_STATUS_URL = "http://api.twitter.com/1/statuses/update.json";

}