package com.springsource.greenhouse.oauth;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.oauth.consumer.token.HttpSessionBasedTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;

public class NetworkConnectionsTokenServicesTest {
    private JdbcTemplate jdbcTemplate;
    private OAuthConsumerToken twitterToken;
    private OAuthConsumerToken linkedInToken;
    private NetworkConnectionsTokenServices tokenServices;
    private MockHttpSession session;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        twitterToken = new OAuthConsumerToken();
        linkedInToken = new OAuthConsumerToken();
        
        session = new MockHttpSession();      
        session.setAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#twitter", twitterToken);

        jdbcTemplate = mock(JdbcTemplate.class);
        List<OAuthConsumerToken> linkedInTokenList = asList(linkedInToken);
        when(jdbcTemplate.query(eq(NetworkConnectionsTokenServices.SELECT_TOKEN_SQL), any(RowMapper.class), eq(1234L), eq("linkedIn"))).
                thenReturn(linkedInTokenList);
        tokenServices = new NetworkConnectionsTokenServices(jdbcTemplate, session, 1234L);
    }
        
    @Test
    public void shouldCreateNetworkConnectionsTokenServices() {
        assertSame(jdbcTemplate, tokenServices.getJdbcTemplate());
        assertEquals(1234L, tokenServices.getUserId().longValue());
    }
      
    @Test
    public void shouldReturnNullTokenForUnknownResource() {
        assertNull(tokenServices.getToken("ohloh"));
    }
      
    @Test
    public void shouldReturnTokenForKnownResourceInSession() {
        assertSame(twitterToken, tokenServices.getToken("twitter"));
    }
    
    @Test
    public void shouldReturnTokenForKnownResourceInDB() {
        assertNull(session.getAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#linkedIn"));
        assertSame(linkedInToken, tokenServices.getToken("linkedIn"));
        assertSame(linkedInToken, session.getAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#linkedIn"));
    }
    
    @Test
    public void shouldStoreRequestToken() {
        OAuthConsumerToken requestToken = new OAuthConsumerToken();
        requestToken.setAccessToken(false);
        requestToken.setResourceId("myspace");
        requestToken.setSecret("someSecret");
        requestToken.setValue("someToken");
        
        tokenServices.storeToken("myspace", requestToken);
        
        assertSame(requestToken, session.getAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#myspace"));        
        verifyZeroInteractions(jdbcTemplate);//.update(NetworkConnectionsTokenServices.INSERT_TOKEN_SQL, "myspace", "someToken", "someSecret");
    }
    
    @Test
    public void shouldStoreAccessToken() {
        OAuthConsumerToken requestToken = new OAuthConsumerToken();
        requestToken.setAccessToken(true);
        requestToken.setResourceId("myspace");
        requestToken.setSecret("someSecret");
        requestToken.setValue("someToken");
        
        tokenServices.storeToken("myspace", requestToken);
        
        assertSame(requestToken, session.getAttribute(HttpSessionBasedTokenServices.KEY_PREFIX + "#myspace"));        
        verify(jdbcTemplate).update(NetworkConnectionsTokenServices.INSERT_TOKEN_SQL, 1234L, "myspace", "someToken", "someSecret");
    }
}
