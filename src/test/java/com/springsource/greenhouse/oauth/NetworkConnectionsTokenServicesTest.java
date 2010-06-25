package com.springsource.greenhouse.oauth;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.HttpSessionBasedTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServices;

import com.springsource.greenhouse.signin.GreenhouseUserDetails;

public class NetworkConnectionsTokenServicesTest {
    private JdbcTemplate jdbcTemplate;
    private NetworkConnectionsTokenServicesFactory tokenServicesFactory;

    @Before
    public void setupDatabase() {
        EmbeddedDatabaseFactory dbFactory = new EmbeddedDatabaseFactory();
        dbFactory.setDatabaseType(EmbeddedDatabaseType.H2);
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("NetworkConnectionsTokenServicesTest.sql", getClass()));
        dbFactory.setDatabasePopulator(populator);
        jdbcTemplate = new JdbcTemplate(dbFactory.getDatabase());
        tokenServicesFactory = new NetworkConnectionsTokenServicesFactory(jdbcTemplate);
    }

    @Test
    public void shouldReturnNullTokenForUnknownResource() {
        GreenhouseUserDetails userDetails = new GreenhouseUserDetails(1L, "habuma", "plano", "Craig");
        Authentication authentication = new TestingAuthenticationToken(userDetails, "plano");
        MockHttpServletRequest request = new MockHttpServletRequest();
        OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
        assertNull(tokenServices.getToken("ohloh"));
    }

    @Test
    public void shouldReturnNullTokenForUnknownUser() {
        GreenhouseUserDetails userDetails = new GreenhouseUserDetails(2L, "rclarkson@vmware.com", "atlanta", "Roy");
        Authentication authentication = new TestingAuthenticationToken(userDetails, "atlanta");
        MockHttpServletRequest request = new MockHttpServletRequest();
        OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
        assertNull(tokenServices.getToken("twitter"));
    }

    @Test
    public void shouldReturnTokenForKnownResourceInDB() {
        GreenhouseUserDetails userDetails = new GreenhouseUserDetails(1L, "habuma", "plano", "Craig");
        Authentication authentication = new TestingAuthenticationToken(userDetails, "plano");
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
        
        GreenhouseUserDetails userDetails = new GreenhouseUserDetails(1L, "habuma", "plano", "Craig");
        Authentication authentication = new TestingAuthenticationToken(userDetails, "plano");
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
      GreenhouseUserDetails userDetails = new GreenhouseUserDetails(1L, "habuma", "plano", "Craig");
      Authentication authentication = new TestingAuthenticationToken(userDetails, "plano");
      OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
      tokenServices.storeToken("myspace", requestToken);      
      assertSame(requestToken, request.getSession().getAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#myspace"));
      assertEquals(0, jdbcTemplate.queryForInt("select count(*) from NetworkConnection where accessToken='someToken'"));
    }
    
    @Test
    public void shouldStoreAccessToken() {
      OAuthConsumerToken requestToken = new OAuthConsumerToken();
      requestToken.setAccessToken(true);
      requestToken.setResourceId("myspace");
      requestToken.setSecret("someSecret");
      requestToken.setValue("someToken"); 
      MockHttpServletRequest request = new MockHttpServletRequest();
      GreenhouseUserDetails userDetails = new GreenhouseUserDetails(1L, "habuma", "plano", "Craig");
      Authentication authentication = new TestingAuthenticationToken(userDetails, "plano");
      OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
      tokenServices.storeToken("myspace", requestToken);      
      assertSame(requestToken, request.getSession().getAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#myspace"));
      assertEquals(1, jdbcTemplate.queryForInt("select count(*) from NetworkConnection where accessToken='someToken'"));
    }
}
