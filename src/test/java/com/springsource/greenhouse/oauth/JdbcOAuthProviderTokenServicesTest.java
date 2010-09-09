package com.springsource.greenhouse.oauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderToken;
import org.springframework.security.password.NoOpPasswordEncoder;
import org.springframework.security.password.PasswordEncoder;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.JdbcAccountRepository;
import com.springsource.greenhouse.account.StubFileStorage;
import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcOAuthProviderTokenServicesTest {

	private EmbeddedDatabase db;
	
    private JdbcOAuthProviderTokenServices tokenServices;

    @Before
    public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().connectedApp().testData(getClass()).getDatabase();
    	JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
		PasswordEncoder passwordEncoder = new NoOpPasswordEncoder();
		JdbcAccountRepository accountRepository = new JdbcAccountRepository(jdbcTemplate, passwordEncoder, new StubFileStorage(), new UriTemplate("http://localhost:8080/members/{id}").toString());
    	tokenServices = new JdbcOAuthProviderTokenServices(jdbcTemplate, accountRepository);
    }
    
	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
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
    	
    	Authentication auth = new TestingAuthenticationToken(testAccount(), "atlanta");
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
    	assertEquals(accessToken.getSecret(), accessToken2.getSecret());
    	assertNull(accessToken2.getCallbackUrl());
    	assertNull(accessToken2.getVerifier());
    	assertNotNull(accessToken2.getUserAuthentication());
    	Account account = (Account) accessToken2.getUserAuthentication().getPrincipal();
    	assertEquals(1L, (Object) account.getId());
    	assertEquals("OAuth", accessToken2.getUserAuthentication().getCredentials());
    }
    
    @Test
    public void shouldReauthorize() {
        // TODO - This test duplicates a large portion of the previous test and needs to be cleaned up. But
        //        I needed to commit this to get the fix under test and wanted to be sure it was in the 
        //        code repo before I left for my flight. I'll certainly revisit this to remove duplication
        //        later.
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
        
        Authentication auth = new TestingAuthenticationToken(testAccount(), "atlanta");
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
        assertEquals(accessToken.getSecret(), accessToken2.getSecret());
        assertNull(accessToken2.getCallbackUrl());
        assertNull(accessToken2.getVerifier());
    	Account account = (Account) accessToken2.getUserAuthentication().getPrincipal();
    	assertEquals(1L, (Object) account.getId());
    	assertEquals("OAuth", accessToken2.getUserAuthentication().getCredentials());
        
        OAuthProviderToken reAuthToken = tokenServices.createUnauthorizedRequestToken("a08318eb478a1ee31f69a55276f3af64", "x-com-springsource-greenhouse://oauth-response");
        assertFalse(reAuthToken.isAccessToken());
        assertNotNull(reAuthToken.getValue());
        assertEquals("a08318eb478a1ee31f69a55276f3af64", reAuthToken.getConsumerKey());
        assertNotNull(reAuthToken.getSecret());
        assertEquals("x-com-springsource-greenhouse://oauth-response", reAuthToken.getCallbackUrl());
        assertNull(reAuthToken.getVerifier());
        
        OAuthProviderToken reAuthToken2 = tokenServices.getToken(reAuthToken.getValue());
        assertEquals(reAuthToken.isAccessToken(), reAuthToken2.isAccessToken());
        assertEquals(reAuthToken.getValue(), reAuthToken2.getValue());
        assertEquals(reAuthToken.getConsumerKey(), reAuthToken2.getConsumerKey());
        assertEquals(reAuthToken.getSecret(), reAuthToken2.getSecret());
        assertEquals(reAuthToken.getCallbackUrl(), reAuthToken2.getCallbackUrl());
        assertEquals(reAuthToken.getVerifier(), reAuthToken.getVerifier());
        
        tokenServices.authorizeRequestToken(reAuthToken.getValue(), "12345", auth);        
        
        OAuthProviderToken reAuthToken3 = tokenServices.getToken(reAuthToken.getValue());
        assertEquals(reAuthToken.isAccessToken(), reAuthToken3.isAccessToken());
        assertEquals(reAuthToken.getValue(), reAuthToken3.getValue());
        assertEquals(reAuthToken.getConsumerKey(), reAuthToken3.getConsumerKey());
        assertEquals(reAuthToken.getSecret(), reAuthToken3.getSecret());
        assertEquals(reAuthToken.getCallbackUrl(), reAuthToken3.getCallbackUrl());
        assertEquals("12345", reAuthToken3.getVerifier());

        OAuthProviderToken reAuthAccessToken = tokenServices.createAccessToken(reAuthToken3.getValue());
        assertEquals(true, reAuthAccessToken.isAccessToken());
        assertFalse(reAuthToken.getValue().equals(reAuthAccessToken.getValue()));
        assertEquals(reAuthToken.getConsumerKey(), reAuthAccessToken.getConsumerKey());
        assertNotNull(reAuthAccessToken.getSecret());
        assertNull(reAuthAccessToken.getVerifier());
        assertNull(reAuthAccessToken.getCallbackUrl());
        
        OAuthAccessProviderToken reAuthAccessToken2 = (OAuthAccessProviderToken) tokenServices.getToken(reAuthAccessToken.getValue());
        assertEquals(reAuthAccessToken.isAccessToken(), reAuthAccessToken2.isAccessToken());
        assertEquals(reAuthAccessToken.getValue(), reAuthAccessToken2.getValue());
        assertEquals(reAuthAccessToken.getConsumerKey(), reAuthAccessToken2.getConsumerKey());
        assertEquals(reAuthAccessToken.getSecret(), reAuthAccessToken2.getSecret());
        assertNull(reAuthAccessToken2.getCallbackUrl());
        assertNull(reAuthAccessToken2.getVerifier());
    	account = (Account) accessToken2.getUserAuthentication().getPrincipal();
    	assertEquals(1L, (Object) account.getId());
    	assertEquals("OAuth", accessToken2.getUserAuthentication().getCredentials());
    }
    
    private Account testAccount() {
    	return new Account(1L, "Roy", "Clarkson", "rclarkson@vmware.com", "roy", "file://pic.jpg", new UriTemplate("http://localhost:8080/members/{profileKey}"));
    }
}