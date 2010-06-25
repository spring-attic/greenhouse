package com.springsource.greenhouse.oauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderToken;

import com.springsource.greenhouse.signin.GreenhouseUserDetails;

public class GreenhouseOAuthProviderTokenServicesTest {
	
    private GreenhouseOAuthProviderTokenServices tokenServices;
    
    @Before
    public void setup() {
    	EmbeddedDatabaseFactory dbFactory = new EmbeddedDatabaseFactory();
    	dbFactory.setDatabaseType(EmbeddedDatabaseType.H2);
    	ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    	populator.addScript(new ClassPathResource("GreenhouseOAuthProviderTokenServicesTest.sql", getClass()));
    	dbFactory.setDatabasePopulator(populator);
    	tokenServices = new GreenhouseOAuthProviderTokenServices(new JdbcTemplate(dbFactory.getDatabase()));
    }
    
    @Test
    public void testOAuthInteraction() {
    	OAuthProviderToken token = tokenServices.createUnauthorizedRequestToken("a08318eb478a1ee31f69a55276f3af64", "x-com-springsource-greenhouse://oauth-response");
    	assertFalse(token.isAccessToken());
    	assertNotNull(token.getValue());
    	assertEquals("a08318eb478a1ee31f69a55276f3af64", token.getConsumerKey());
    	assertNotNull(token.getSecret());
    	assertEquals("x-com-springsource-greenhouse://oauth-response", token.getCallbackUrl());
    	assertNull(token.getVerifier());
    	
    	OAuthProviderToken token2 = tokenServices.getToken(token.getValue());
    	assertEquals(token.isAccessToken(), token2.isAccessToken());
    	assertEquals(token.getValue(), token2.getValue());
    	assertEquals(token.getConsumerKey(), token2.getConsumerKey());
    	assertEquals(token.getSecret(), token2.getSecret());
    	assertEquals(token.getCallbackUrl(), token2.getCallbackUrl());
    	assertEquals(token.getVerifier(), token.getVerifier());
    	
    	Authentication auth = new TestingAuthenticationToken(new GreenhouseUserDetails(1L, "rclarkson@vmware.com", "rclarkson", "Roy"), "atlanta");
    	tokenServices.authorizeRequestToken(token.getValue(), "12345", auth);

    	OAuthProviderToken token3 = tokenServices.getToken(token.getValue());
    	assertEquals(token.isAccessToken(), token3.isAccessToken());
    	assertEquals(token.getValue(), token3.getValue());
    	assertEquals(token.getConsumerKey(), token3.getConsumerKey());
    	assertEquals(token.getSecret(), token3.getSecret());
    	assertEquals(token.getCallbackUrl(), token3.getCallbackUrl());
    	assertEquals("12345", token3.getVerifier());

    	OAuthProviderToken accessToken = tokenServices.createAccessToken(token3.getValue());
    	assertEquals(true, accessToken.isAccessToken());
    	assertFalse(token.getValue().equals(accessToken.getValue()));
    	assertEquals(token.getConsumerKey(), accessToken.getConsumerKey());
    	assertNotNull(accessToken.getSecret());
    	assertNull(accessToken.getVerifier());
    	assertNull(accessToken.getCallbackUrl());
    	
    	OAuthAccessProviderToken accessToken2 = (OAuthAccessProviderToken) tokenServices.getToken(accessToken.getValue());
    	assertEquals(accessToken.isAccessToken(), accessToken2.isAccessToken());
    	assertEquals(accessToken.getValue(), accessToken2.getValue());
    	assertEquals(accessToken.getConsumerKey(), accessToken2.getConsumerKey());
    	assertNull(accessToken2.getSecret());
    	assertNull(accessToken2.getCallbackUrl());
    	assertNull(accessToken2.getVerifier());
    	assertNotNull(accessToken2.getUserAuthentication());
    	assertEquals("doesnt-matter-yet", accessToken2.getUserAuthentication().getPrincipal());
    	assertEquals("doesnt-matter-yet", accessToken2.getUserAuthentication().getCredentials());
    }
    
}