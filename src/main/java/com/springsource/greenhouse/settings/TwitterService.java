package com.springsource.greenhouse.settings;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth.consumer.OAuthConsumerSupport;
import org.springframework.security.oauth.consumer.ProtectedResourceDetails;
import org.springframework.security.oauth.consumer.ProtectedResourceDetailsService;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class TwitterService {
	Logger logger = LoggerFactory.getLogger(getClass());

	static final String UPDATE_STATUS_URL = "http://api.twitter.com/1/statuses/update.json";
	static final String VERIFY_CREDENTIALS_URL = "http://api.twitter.com/1/account/verify_credentials.json";
	static final String FRIENDS_IDS_URL = "http://api.twitter.com/1/friends/ids.json?screen_name={screen_name}";
	static final String FRIENDS_STATUSES_URL = "http://api.twitter.com/1/statuses/friends.json?screen_name={screen_name}";
	static final String USER_INFO_URL = "http://api.twitter.com/1/users/show.json?user_id={user_id}";

	private OAuthConsumerSupport oauthSupport;
	private ProtectedResourceDetailsService resourceDetailsService;

	@Inject
	public TwitterService(OAuthConsumerSupport oauthSupport, ProtectedResourceDetailsService resourceDetailsService) {
		this.oauthSupport = oauthSupport;
		this.resourceDetailsService = resourceDetailsService;
		this.restTemplate = new RestTemplate();
	}

	public void updateStatus(OAuthConsumerToken accessToken, String message) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("status", message);
		exchangeWithTwitterForMap(accessToken, HttpMethod.POST, UPDATE_STATUS_URL, parameters);
	}

	public String getScreenName(OAuthConsumerToken accessToken) {
		Map<?,?> response = exchangeWithTwitterForMap(accessToken, HttpMethod.GET, VERIFY_CREDENTIALS_URL,
		        new HashMap<String, String>());
		return (String) response.get("screen_name");
	}
	
	public String[] getFriends(OAuthConsumerToken accessToken, String screenName) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("screen_name", screenName);
		List<?> followingResponse = exchangeWithTwitterForList(accessToken, HttpMethod.GET, FRIENDS_STATUSES_URL, parameters);
		String[] friends = new String[followingResponse.size()];
		
		for(int i=0; i<followingResponse.size(); i++) {
			Map<?, ?> item = (Map<?, ?>) followingResponse.get(i);
			friends[i] = (String) item.get("screen_name");
		}
		
		return friends;
	}

	private Map<?,?> exchangeWithTwitterForMap(OAuthConsumerToken accessToken, HttpMethod method, String url,
	        Map<String, String> parameters) {
		return exchangeWithTwitter(accessToken, method, url, parameters, Map.class);
	}

	private List<?> exchangeWithTwitterForList(OAuthConsumerToken accessToken, HttpMethod method, String url,
	        Map<String, String> parameters) {
		return exchangeWithTwitter(accessToken, method, url, parameters, List.class);
	}

	private <T> T exchangeWithTwitter(OAuthConsumerToken accessToken, HttpMethod method, String twitterUrl,
		        Map<String, String> parameters, Class<T> responseType) {

		HttpHeaders headers = buildRequestHeaders(accessToken, method, twitterUrl, parameters);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		HashMap<String, Object> uriVariables = new HashMap<String, Object>();

		if(method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT)) {
			for (String key : parameters.keySet()) {
				form.add(key, parameters.get(key));
			}
		} else {
			for (String key : parameters.keySet()) {
				uriVariables.put(key, parameters.get(key));
			}				
		}

		HttpEntity<MultiValueMap<String, String>> statusEntity = 
			    new HttpEntity<MultiValueMap<String, String>>(form, headers);

		return restTemplate.exchange(twitterUrl, method, statusEntity, responseType, uriVariables).getBody();
	}

	private HttpHeaders buildRequestHeaders(OAuthConsumerToken accessToken, HttpMethod method, String twitterUrl,
            Map<String, String> parameters) {
	    try {
	        ProtectedResourceDetails details = resourceDetailsService.loadProtectedResourceDetailsById("twitter");
	        String authorizationHeader = oauthSupport.getAuthorizationHeader(details, accessToken, new URL(twitterUrl), 
	        		method.name(), parameters);
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Authorization", authorizationHeader);
	        headers.add("Content-Type", "application/x-www-form-urlencoded");
	        return headers;
        } catch (MalformedURLException e) {
        	throw new RestClientException("Malformed URL: " +twitterUrl, e);
        }
    }

	private RestTemplate restTemplate;
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
