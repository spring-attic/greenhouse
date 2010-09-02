package com.springsource.greenhouse.oauth;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.HttpSessionBasedTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServices;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class OAuthConsumerTokenServicesAdapterTest {

	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;
    
	private OAuthConsumerTokenServicesAdapterFactory tokenServicesFactory;

    @Before
    public void setupDatabase() {
		db = new GreenhouseTestDatabaseBuilder().member().connectedAccount().testData(getClass()).getDatabase();
        jdbcTemplate = new JdbcTemplate(db);

		JdbcAccessTokenServices accessTokenServices = new JdbcAccessTokenServices(jdbcTemplate);
		tokenServicesFactory = new OAuthConsumerTokenServicesAdapterFactory(accessTokenServices);
    }

	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}
    
    @Test
    public void shouldReturnNullTokenForUnknownResource() {
        Authentication authentication = new TestingAuthenticationToken(createAccount(), "plano");
        MockHttpServletRequest request = new MockHttpServletRequest();
        OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
        assertNull(tokenServices.getToken("ohloh"));
    }


    @Test
    public void shouldReturnNullTokenForUnknownUser() {
        Authentication authentication = new TestingAuthenticationToken(new Account(2L, "Roy", "Clarkson", "rclarkson@vmware.com", "roy", "file://pic.jpg"), "atlanta");
        MockHttpServletRequest request = new MockHttpServletRequest();
        OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
        assertNull(tokenServices.getToken("twitter"));
    }

    @Test
    public void shouldReturnTokenForKnownResourceInDB() {
        Authentication authentication = new TestingAuthenticationToken(createAccount(), "plano");
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
        
        Authentication authentication = new TestingAuthenticationToken(createAccount(), "plano");
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
      Authentication authentication = new TestingAuthenticationToken(createAccount(), "plano");
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
      Authentication authentication = new TestingAuthenticationToken(createAccount(), "plano");
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
        Authentication authentication = new TestingAuthenticationToken(createAccount(), "plano");        
        request.getSession().setAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#twitter", accessToken);
        
        // Token should be available before remove
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount where accessToken = 'twitterToken'"));
        assertNotNull(request.getSession().getAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#twitter"));

        OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
        ((OAuthConsumerTokenServicesAdapter) tokenServices).removeToken("twitter");
        
        // Token should be gone after remove
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount where accessToken = 'twitterToken'"));
        assertNull(request.getSession().getAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#twitter"));
    }
    
	private Account createAccount() {
		return new Account(1L, "Craig", "Walls", "craig@habuma.com", "habuma", "file://pic.jpg");
	}
	
}
