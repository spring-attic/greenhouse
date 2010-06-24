package com.springsource.greenhouse.oauth;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenImpl;

public class GreenhouseOAuthProviderTokenServicesTest {

	@Test
	public void shouldCreateAnUnauthorizedTokenAndInsertItIntoDatabase() {
	    JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);	    
	    
		GreenhouseOAuthProviderTokenServices tokenServices = new GreenhouseOAuthProviderTokenServices(jdbcTemplate);
		OAuthProviderTokenImpl token = (OAuthProviderTokenImpl) tokenServices.createUnauthorizedRequestToken("consumerKey", "http://callbackurl");
		assertEquals("http://callbackurl", token.getCallbackUrl());
		assertEquals("consumerKey", token.getConsumerKey());
		
		verify(jdbcTemplate).update(GreenhouseOAuthProviderTokenServices.INSERT_TOKEN_URL, 
		        token.getValue(), token.getConsumerKey(), token.getSecret(), token.getCallbackUrl(), token.getTimestamp());
	}
	
	
}
