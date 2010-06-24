package com.springsource.greenhouse.oauth;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenImpl;

import com.springsource.greenhouse.signin.GreenhouseUserDetails;

public class GreenhouseOAuthProviderTokenServicesTest {
    private JdbcTemplate jdbcTemplate;
    private GreenhouseOAuthProviderTokenServices tokenServices;
    
	@Test
	public void shouldCreateAnUnauthorizedTokenAndInsertItIntoDatabase() {
	    prepareTokenServices();
		
		OAuthProviderTokenImpl token = (OAuthProviderTokenImpl) tokenServices.createUnauthorizedRequestToken("consumerKey", "http://callbackurl");
		assertEquals("http://callbackurl", token.getCallbackUrl());
		assertEquals("consumerKey", token.getConsumerKey());
		
		verify(jdbcTemplate).update(GreenhouseOAuthProviderTokenServices.INSERT_REQUEST_TOKEN_SQL, 
		        token.getValue(), token.getConsumerKey(), token.getSecret(), token.getCallbackUrl(), token.getTimestamp());
	}
	
	@Test
	public void shouldAuthorizeToken() {
        prepareTokenServices();
        
        Authentication authentication = mock(Authentication.class);
        GreenhouseUserDetails userDetails = new GreenhouseUserDetails(1234L, "someuser", "password", "Chuck");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        
        tokenServices.authorizeRequestToken("someToken", "someVerifier", authentication);
        
        verify(jdbcTemplate).update(eq(GreenhouseOAuthProviderTokenServices.AUTHORIZE_TOKEN_SQL),
                eq("someVerifier"), any(Long.class), eq(1234L), eq("someToken"));
	}
	
	@Test
	public void shouldCreateAccessToken() {
        prepareTokenServices();
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("consumerKey", "someKey");
        resultMap.put("userId", 1234L);
        when(jdbcTemplate.queryForMap(GreenhouseOAuthProviderTokenServices.SELECT_REQUEST_TOKEN_DETAILS_SQL, "requestToken")).thenReturn(resultMap);
        
        OAuthAccessProviderToken token = tokenServices.createAccessToken("requestToken");
        
        assertNotNull(token.getValue());
        assertEquals("someKey", token.getConsumerKey());
        assertNotNull(token.getSecret());
        verify(jdbcTemplate).update(GreenhouseOAuthProviderTokenServices.DELETE_REQUEST_TOKEN_SQL, "requestToken");
        verify(jdbcTemplate).update(GreenhouseOAuthProviderTokenServices.INSERT_ACCESS_TOKEN_SQL, 1234L, "someKey", token.getValue());
	}
	
    @Test
    @SuppressWarnings("unchecked")
	public void shouldGetRequestToken() {
	    prepareTokenServices();
	    
	    OAuthProviderTokenImpl expectedRequestToken = new OAuthProviderTokenImpl();
	    
	    when(jdbcTemplate.queryForInt(GreenhouseOAuthProviderTokenServices.SELECT_REQUEST_TOKEN_COUNT_SQL, "requestToken")).thenReturn(1);
	    when(jdbcTemplate.queryForObject(eq(GreenhouseOAuthProviderTokenServices.SELECT_REQUEST_TOKEN_SQL), any(RowMapper.class), eq("requestToken"))).
	            thenReturn(expectedRequestToken);
	    
	    assertSame(expectedRequestToken, tokenServices.getToken("requestToken"));
	    assertNotSame(expectedRequestToken, tokenServices.getToken("accessToken"));
	}
    
    @Test
    @SuppressWarnings("unchecked")
    public void shouldGetAccessToken() {
        prepareTokenServices();
        
        OAuthProviderTokenImpl expectedAccessToken = new OAuthProviderTokenImpl();
        
        when(jdbcTemplate.queryForInt(GreenhouseOAuthProviderTokenServices.SELECT_REQUEST_TOKEN_COUNT_SQL, "accessToken")).thenReturn(0);
        when(jdbcTemplate.queryForObject(eq(GreenhouseOAuthProviderTokenServices.SELECT_ACCESS_TOKEN_DETAILS_SQL), any(RowMapper.class), eq("accessToken"))).
                thenReturn(expectedAccessToken);
        
        assertSame(expectedAccessToken, tokenServices.getToken("accessToken"));
        assertNotSame(expectedAccessToken, tokenServices.getToken("requestToken"));
    }
	
    private void prepareTokenServices() {
        jdbcTemplate = mock(JdbcTemplate.class);                
        tokenServices = new GreenhouseOAuthProviderTokenServices(jdbcTemplate);
    }
}
