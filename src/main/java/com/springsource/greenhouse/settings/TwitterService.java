package com.springsource.greenhouse.settings;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth.consumer.OAuthConsumerSupport;
import org.springframework.security.oauth.consumer.ProtectedResourceDetails;
import org.springframework.security.oauth.consumer.ProtectedResourceDetailsService;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TwitterService {
	
    Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String UPDATE_STATUS_URL = "http://localhost:8081/1/statuses/update.json";

    private OAuthConsumerSupport oauthSupport;
    private ProtectedResourceDetailsService resourceDetailsService;

    @Inject
    public TwitterService(OAuthConsumerSupport oauthSupport, ProtectedResourceDetailsService resourceDetailsService) {
        this.oauthSupport = oauthSupport;
        this.resourceDetailsService = resourceDetailsService;
    }
    
    public void updateStatus(OAuthConsumerToken accessToken, String message) {
        updateStatusUsingRestTemplate(accessToken, message);
//        try {
//            oauthSupport.readProtectedResource(new URL(UPDATE_STATUS_URL + "?status=" + message), accessToken, "POST");
//        } catch (Exception e) {
//            logger.error("Unabled to update Twitter status. Reason: " + e.getMessage());
//        }
    }
    
    public void updateStatusUsingRestTemplate(OAuthConsumerToken accessToken, String message) {
        try {
            String messageToSend = message + System.currentTimeMillis();
            ProtectedResourceDetails details = resourceDetailsService.loadProtectedResourceDetailsById("twitter");
            Map<String, String> additionalParameters = new HashMap<String, String>();
            String acceptHeader = oauthSupport.getAuthorizationHeader(details, accessToken, new URL(UPDATE_STATUS_URL), "POST", additionalParameters);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", acceptHeader); 
            TwitterStatus status = new TwitterStatus();
            status.setStatus(messageToSend);
            HttpEntity<TwitterStatus> statusEntity = new HttpEntity<TwitterStatus>(status, headers);
            
            RestTemplate rest = new RestTemplate();
            rest.postForEntity(UPDATE_STATUS_URL, statusEntity, String.class, new HashMap<String, Object>());
        } catch (Exception e) {
            logger.error("Unabled to update Twitter status. Reason: " + e.getMessage());
        }
    }
}
