package com.springsource.greenhouse.settings;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth.consumer.OAuthConsumerSupport;
import org.springframework.security.oauth.consumer.ProtectedResourceDetails;
import org.springframework.security.oauth.consumer.ProtectedResourceDetailsService;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class TwitterService {
	Logger logger = LoggerFactory.getLogger(getClass());

	static final String UPDATE_STATUS_URL = "http://api.twitter.com/1/statuses/update.json";
	static final String VERIFY_CREDENTIALS_URL = "http://api.twitter.com/1/account/verify_credentials.json";

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
		exchangeWithTwitter(accessToken, HttpMethod.POST, UPDATE_STATUS_URL, parameters);
	}

	public String getTwitterUsername(OAuthConsumerToken accessToken) {
		Map response = exchangeWithTwitter(accessToken, HttpMethod.GET, VERIFY_CREDENTIALS_URL,
		        new HashMap<String, String>());
		return (String) response.get("screen_name");
	}

	private Map exchangeWithTwitter(OAuthConsumerToken accessToken, HttpMethod method, String url,
	        Map<String, String> parameters) {
		try {
			ProtectedResourceDetails details = resourceDetailsService.loadProtectedResourceDetailsById("twitter");
			String authorizationHeader = oauthSupport.getAuthorizationHeader(details, accessToken, new URL(url), 
					method.name(), parameters);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", authorizationHeader);
			headers.add("Content-Type", "application/x-www-form-urlencoded");
			MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
			for (String key : parameters.keySet()) {
				form.add(key, parameters.get(key));
			}

			HttpEntity<MultiValueMap<String, String>> statusEntity = 
				    new HttpEntity<MultiValueMap<String, String>>(form, headers);

			// TODO : May need to populate this for other types of Twitter requests
			HashMap<String, Object> uriVariables = new HashMap<String, Object>();
			
			return restTemplate.exchange(url, method, statusEntity, Map.class, uriVariables).getBody();
		} catch (HttpClientErrorException e) {
			// TODO : Handle this exception more generically
			if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
				// this is normal and expected if two or more duplicate tweets are sent back-to-back
				logger.info("Tweet forbidden. Probably due to a duplicate tweet or a rate limit being reached.");
			} else {
				logger.error("Unable to update Twitter status. Reason: " + e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Unable to update Twitter status. Reason: " + e.getMessage());
		}
		return Collections.emptyMap();
	}

	private RestTemplate restTemplate;
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
