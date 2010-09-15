package com.springsource.greenhouse.oauth.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.encrypt.NoOpPasswordEncoder;
import org.springframework.security.encrypt.SearchableStringEncryptor;
import org.springframework.security.oauth.provider.token.InvalidOAuthTokenException;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderToken;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.InvalidAccessTokenException;
import com.springsource.greenhouse.account.JdbcAccountRepository;
import com.springsource.greenhouse.account.StubFileStorage;
import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class OAuthSessionManagerProviderTokenServicesTest {
	
	private EmbeddedDatabase db;

	private OAuthSessionManagerProviderTokenServices tokenServices;
	
	@Before
	public void setUp() {
    	db = new GreenhouseTestDatabaseBuilder().member().connectedApp().testData(StandardOAuthSessionManagerTest.class).getDatabase();		
    	AccountRepository accountRepository = new JdbcAccountRepository(new JdbcTemplate(db), new SearchableStringEncryptor("secret", "5b8bd7612cdab5ed"), NoOpPasswordEncoder.getInstance(), new StubFileStorage(), "http://localhost:8080/members/{profileKey}");
		OAuthSessionManager sessionManager = new StandardOAuthSessionManager(accountRepository);
		tokenServices = new OAuthSessionManagerProviderTokenServices(sessionManager, accountRepository);
	}
	
	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}
	
	@Test
	public void oAuth10SessionLifecycle() throws InvalidRequestTokenException, InvalidAccessTokenException {
		executeOAuthSessionLifecycle(2);
	}

	private void executeOAuthSessionLifecycle(int numberOfTimes) throws InvalidRequestTokenException, InvalidAccessTokenException {
		for (int i = 0; i < numberOfTimes; i++) {
			OAuthProviderToken token = tokenServices.createUnauthorizedRequestToken("123456789", "x-com-springsource-greenhouse://oauth-response");
			assertEquals("123456789", token.getConsumerKey());
			assertEquals("x-com-springsource-greenhouse://oauth-response", token.getCallbackUrl());
			String requestToken = token.getValue();
			String secret = token.getSecret();
			assertNotNull(requestToken);
			assertNotNull(secret);
			assertNull(token.getVerifier());
			
			token = tokenServices.getToken(token.getValue());
			assertEquals("123456789", token.getConsumerKey());
			assertEquals("x-com-springsource-greenhouse://oauth-response", token.getCallbackUrl());
			assertEquals(requestToken, token.getValue());
			assertEquals(secret, token.getSecret());
			assertNull(token.getVerifier());

			Account account = new Account(1L, "Roy", "Clarkson", "rclarkson@vmware.com", null, null, null);
			Authentication auth = new UsernamePasswordAuthenticationToken(account, null, (Collection<GrantedAuthority>)null);		
			tokenServices.authorizeRequestToken(requestToken, "verifier", auth);
			
			token = tokenServices.getToken(token.getValue());			
			assertEquals("123456789", token.getConsumerKey());
			assertEquals("x-com-springsource-greenhouse://oauth-response", token.getCallbackUrl());
			assertEquals(requestToken, token.getValue());
			assertEquals(secret, token.getSecret());
			assertEquals("verifier", token.getVerifier());
			
			OAuthAccessProviderToken accessToken = tokenServices.createAccessToken(token.getValue());
			assertEquals("123456789", accessToken.getConsumerKey());
			assertNotNull(accessToken.getValue());
			assertFalse(requestToken.equals(accessToken.getValue()));
			assertNotNull(accessToken.getSecret());
			assertFalse(secret.equals(accessToken.getSecret()));
			
			try {
				tokenServices.getToken(requestToken);
				fail("Should have thrown exception");
			} catch (InvalidOAuthTokenException e) {
				
			}
			
			Authentication auth2 = accessToken.getUserAuthentication();
			assertNotNull(auth2);
			assertTrue(auth2.getPrincipal() instanceof Account);
			assertEquals((Long) 1L, ((Account)auth2.getPrincipal()).getId());
		}
	}
}
