package com.springsource.greenhouse.oauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.HttpSessionBasedTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServices;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.signup.GreenhouseTestUserDatabaseFactory;

public class GreenhouseOAuthConsumerTokenServicesTest {

	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;
    
	private GreenhouseOAuthConsumerTokenServicesFactory tokenServicesFactory;

    @Before
    public void setupDatabase() {
    	db = GreenhouseTestUserDatabaseFactory.createUserDatabase(new ClassPathResource("GreenhouseOAuthConsumerTokenServicesTest.sql", getClass()));
        jdbcTemplate = new JdbcTemplate(db);
        tokenServicesFactory = new GreenhouseOAuthConsumerTokenServicesFactory(jdbcTemplate);
    }

    @After
    public void destroy() {
    	db.shutdown();
    }
    
    @Test
    public void shouldReturnNullTokenForUnknownResource() {
        Authentication authentication = new TestingAuthenticationToken(new Account(1L), "plano");
        MockHttpServletRequest request = new MockHttpServletRequest();
        OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
        assertNull(tokenServices.getToken("ohloh"));
    }

    @Test
    public void shouldReturnNullTokenForUnknownUser() {
        Authentication authentication = new TestingAuthenticationToken(new Account(2L), "atlanta");
        MockHttpServletRequest request = new MockHttpServletRequest();
        OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
        assertNull(tokenServices.getToken("twitter"));
    }

    @Test
    public void shouldReturnTokenForKnownResourceInDB() {
        Authentication authentication = new TestingAuthenticationToken(new Account(1L), "plano");
        MockHttpServletRequest request = new MockHttpServletRequest();
        OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
        OAuthConsumerToken token = tokenServices.getToken("twitter");
        assertNotNull(token);
        assertEquals("twitter", token.getResourceId());
        assertEquals("twitterToken", token.getValue());
        assertEquals("twitterTokenSecret", token.getSecret());
    }

    @Test
    public void shouldReturnTokenForKnownResourceInSession() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        OAuthConsumerToken linkedInToken = new OAuthConsumerToken();
        request.getSession().setAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#linkedIn", linkedInToken);
        
        Authentication authentication = new TestingAuthenticationToken(new Account(1L), "plano");
        OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
        OAuthConsumerToken token = tokenServices.getToken("linkedIn");
        assertSame(linkedInToken, token);
    }
    
    @Test
    public void shouldStoreRequestToken() {
      OAuthConsumerToken requestToken = new OAuthConsumerToken();
      requestToken.setAccessToken(false);
      requestToken.setResourceId("myspace");
      requestToken.setSecret("someSecret");
      requestToken.setValue("someToken"); 
      MockHttpServletRequest request = new MockHttpServletRequest();
      Authentication authentication = new TestingAuthenticationToken(new Account(1L), "plano");
      OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
      tokenServices.storeToken("myspace", requestToken);      
      assertSame(requestToken, request.getSession().getAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#myspace"));
      assertEquals(0, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount where accessToken = 'someToken'"));
    }
    
    @Test
    public void shouldStoreAccessToken() {
      OAuthConsumerToken requestToken = new OAuthConsumerToken();
      requestToken.setAccessToken(true);
      requestToken.setResourceId("myspace");
      requestToken.setSecret("someSecret");
      requestToken.setValue("someToken"); 
      MockHttpServletRequest request = new MockHttpServletRequest();
      Authentication authentication = new TestingAuthenticationToken(new Account(1L), "plano");
      OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
      tokenServices.storeToken("myspace", requestToken);      
      assertSame(requestToken, request.getSession().getAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#myspace"));
      assertEquals(1, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount where accessToken='someToken'"));
    }
    
    @Test
    public void shouldRemoveToken() {
        OAuthConsumerToken accessToken = new OAuthConsumerToken();
        accessToken.setAccessToken(true);
        accessToken.setResourceId("twitter");
        accessToken.setSecret("twitterTokenSecret");
        accessToken.setValue("twitterToken"); 

        MockHttpServletRequest request = new MockHttpServletRequest();
        Authentication authentication = new TestingAuthenticationToken(new Account(1L), "plano");        
        request.getSession().setAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#twitter", accessToken);
        
        // Token should be available before remove
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount where accessToken = 'twitterToken'"));
        assertNotNull(request.getSession().getAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#twitter"));

        OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
        ((GreenhouseOAuthConsumerTokenServices) tokenServices).removeToken("twitter");
        
        // Token should be gone after remove
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from ConnectedApp where accessToken = 'twitterToken'"));
        assertNull(request.getSession().getAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#twitter"));
    }
}
