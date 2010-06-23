package com.springsource.greenhouse.oauth;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth.consumer.token.HttpSessionBasedTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;

public class GreenhouseOAuthConsumerTokenServices extends HttpSessionBasedTokenServices {
    private String userName;
    public GreenhouseOAuthConsumerTokenServices(HttpSession session, String userName) {
        super(session);
        this.userName = userName;
    }
    
    @Override
    public OAuthConsumerToken getToken(String resourceId)
            throws AuthenticationException {
        
        OAuthConsumerToken token = super.getToken(resourceId);
        if(token == null) {
            token = getTokenFromDatabase(resourceId, userName);
            if(token != null) {
                super.storeToken(resourceId, token);
            }
        }
        return token;
    }
    
    @Override
    public void storeToken(String resourceId, OAuthConsumerToken token) {
        
        // Don't bother storing request tokens
        if(token.isAccessToken()) {
            storeTokenInDB(resourceId, resourceId, token);
        }
        
        super.storeToken(resourceId, token);
    }
    
    private OAuthConsumerToken getTokenFromDatabase(String resourceId, String userName) {
        return null; // TODO : Implement
    }
    
    private void storeTokenInDB(String resourceId, String userName, OAuthConsumerToken token) {
        // TODO : Implement
    }
}
