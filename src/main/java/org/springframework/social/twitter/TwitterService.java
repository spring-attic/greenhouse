package org.springframework.social.twitter;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import org.springframework.util.NumberUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class TwitterService implements TwitterOperations {
	
	private static final int DEFAULT_RESULTS_PER_PAGE = 50;

	private OAuthConsumerSupport oauthSupport;
	
	private ProtectedResourceDetailsService resourceDetailsService;
	
	private RestTemplate restTemplate;
	
	private DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

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
	
	public SearchResults search(OAuthConsumerToken accessToken, String query) {
		return search(accessToken, query, 1, DEFAULT_RESULTS_PER_PAGE, 0, 0);
	}

	public SearchResults search(OAuthConsumerToken accessToken, String query, int page, int resultsPerPage) {
		return search(accessToken, query, page, resultsPerPage, 0, 0);
	}

	public SearchResults search(OAuthConsumerToken accessToken, String query, int page, int resultsPerPage, int sinceId, int maxId) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("query", query);
		parameters.put("rpp", String.valueOf(resultsPerPage));
		parameters.put("page", String.valueOf(page));
		
		String searchUrl = SEARCH_URL;
		if(sinceId > 0) {
			searchUrl += "&since_id={since}";
			parameters.put("since", String.valueOf(sinceId));
		}		
		if(maxId > 0) {
			searchUrl += "&max_id={max}";
			parameters.put("max", String.valueOf(maxId));
		}
		
		Map<String, Object> response = exchangeForMap(accessToken, HttpMethod.GET, searchUrl, parameters);		
		List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("results");		
		List<Tweet> tweets = new ArrayList<Tweet>(response.size());
		for (Map<String, Object> item : items) {
	        tweets.add(populateTweet(item));	        
        }
		
		SearchResults results = new SearchResults();
		results.setMaxId(NumberUtils.parseNumber(ObjectUtils.nullSafeToString(response.get("max_id")), Long.class));
		results.setSinceId(NumberUtils.parseNumber(ObjectUtils.nullSafeToString(response.get("since_id")), Long.class));
		Object total = response.get("total");
		if(total != null) {
			results.setTotal(NumberUtils.parseNumber(ObjectUtils.nullSafeToString(response.get("total")), Integer.class));
		}
		results.setLastPage(response.get("next_page") != null);
		results.setTweets(tweets);
		
	    return results;		
	}

	private Tweet populateTweet(Map<String, Object> item) {
	    Tweet tweet = new Tweet();
	    tweet.setId(NumberUtils.parseNumber(ObjectUtils.nullSafeToString(item.get("id")), Long.class));
	    tweet.setFromUser(ObjectUtils.nullSafeToString(item.get("from_user")));
	    tweet.setText(ObjectUtils.nullSafeToString(item.get("text")));
	    tweet.setCreatedAt(toDate(ObjectUtils.nullSafeToString(item.get("created_at"))));
	    tweet.setFromUserId(NumberUtils.parseNumber(ObjectUtils.nullSafeToString(item.get("from_user_id")), Long.class));
	    Object toUserId = item.get("to_user_id");
	    if(toUserId != null) {
	    	tweet.setToUserId(NumberUtils.parseNumber(ObjectUtils.nullSafeToString(toUserId), Long.class));
	    }
	    tweet.setLanguageCode(ObjectUtils.nullSafeToString(item.get("iso_language_code")));
	    tweet.setProfileImageUrl(ObjectUtils.nullSafeToString(item.get("profile_image_url")));
	    tweet.setSource(ObjectUtils.nullSafeToString(item.get("source")));
	    return tweet;
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
	
	private Date toDate(String dateString) {
		try {
	        return dateFormat.parse(dateString);
        } catch (ParseException e) {
        	return null;
        }
	}

	// to support unit testing

	void setRestTemplate(RestTemplate mock) {
		this.restTemplate = mock;
	}
	
	static final String VERIFY_CREDENTIALS_URL = "http://api.twitter.com/1/account/verify_credentials.json";
	static final String FRIENDS_STATUSES_URL = "http://api.twitter.com/1/statuses/friends.json?screen_name={screen_name}";
	static final String UPDATE_STATUS_URL = "http://api.twitter.com/1/statuses/update.json";
	static final String SEARCH_URL = "http://search.twitter.com/search.json?q={query}&rpp={rpp}&page={page}";

}