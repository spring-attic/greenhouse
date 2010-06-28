package com.springsource.greenhouse.settings;

import java.net.URL;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth.consumer.OAuthConsumerSupport;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.stereotype.Component;

@Component
public class TwitterService {
    Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String UPDATE_STATUS_URL = "http://api.twitter.com/1/statuses/update.json";

    private OAuthConsumerSupport oauthSupport;

    @Inject
    public TwitterService(OAuthConsumerSupport oauthSupport) {
        this.oauthSupport = oauthSupport;
    }
    
    public void updateStatus(OAuthConsumerToken accessToken, String message) {
        try {
            oauthSupport.readProtectedResource(new URL(UPDATE_STATUS_URL + "?status=" + message), accessToken, "POST");
        } catch (Exception e) {
            logger.error("Unabled to update Twitter status. Reason: " + e.getMessage());
        }
    }
}
