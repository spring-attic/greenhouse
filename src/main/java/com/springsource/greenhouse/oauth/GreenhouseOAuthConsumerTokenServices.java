package com.springsource.greenhouse.oauth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth.consumer.token.HttpSessionBasedTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;

import com.springsource.greenhouse.utils.EmailUtils;

public class GreenhouseOAuthConsumerTokenServices extends HttpSessionBasedTokenServices {
    private String userName;
    private JdbcTemplate jdbcTemplate;
    
    public GreenhouseOAuthConsumerTokenServices(JdbcTemplate jdbcTemplate, HttpSession session, String userName) {
        super(session);
        this.userName = userName;
        this.jdbcTemplate = jdbcTemplate;
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
        
        // Don't bother storing request tokens in the DB...session-storage is fine
        if(token.isAccessToken()) {
            storeTokenInDB(userName, token);
        }
        
        super.storeToken(resourceId, token);
    }
    
    private OAuthConsumerToken getTokenFromDatabase(String resourceId, String userName) {
        String userId = getUserId(userName);
        
        List<OAuthConsumerToken> accessTokens = jdbcTemplate.query("select resource, tokenValue, secret from OAuthConsumerToken where userId=? and resource=?", 
            new RowMapper<OAuthConsumerToken>() {
                public OAuthConsumerToken mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    OAuthConsumerToken token = new OAuthConsumerToken();
                    token.setAccessToken(true); // we don't persist anything else
                    token.setResourceId(rs.getString("resource"));
                    token.setValue(rs.getString("tokenValue"));
                    token.setSecret(rs.getString("secret"));
                    return token;
                }
        }, userId, resourceId);
        
        OAuthConsumerToken accessToken = null;
        if(accessTokens.size() > 0) {
            accessToken = accessTokens.get(0);
            System.out.println("***************GOT ME AN ACCESS TOKEN FROM THE DB!!!   " + accessToken.getValue());
        }
        
        return accessToken;
    }
    
    private void storeTokenInDB(String userName, OAuthConsumerToken token) {
        jdbcTemplate.update("insert into OAuthConsumerToken (userId, resource, tokenValue, secret) values (?, ?, ?, ?)", 
                getUserId(userName), token.getResourceId(), token.getValue(), token.getSecret());
    }
    
    private String getUserId(String userName) {
        if(EmailUtils.isEmail(userName)) {
            return jdbcTemplate.queryForObject("select id from User where email = ?", String.class, userName);
        } else {
            return jdbcTemplate.queryForObject("select id from User where username = ?", String.class, userName);
        }
    }
}
