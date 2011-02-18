package com.springsource.greenhouse.develop.oauth;

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
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth.provider.token.InvalidOAuthTokenException;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderToken;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountMapper;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.JdbcAccountRepository;
import com.springsource.greenhouse.account.StubFileStorage;
import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;
import com.springsource.greenhouse.develop.AppRepository;
import com.springsource.greenhouse.develop.JdbcAppRepository;
import com.springsource.greenhouse.develop.NoSuchAccountConnectionException;

public class OAuthSessionManagerProviderTokenServicesTest {

	private EmbeddedDatabase db;

	private OAuthSessionManagerProviderTokenServices tokenServices;

	@Before
	public void setUp() {
		db = new GreenhouseTestDatabaseBuilder().member().connectedApp().testData(ConcurrentMapOAuthSessionManagerTest.class).getDatabase();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
		AppRepository appRepository = new JdbcAppRepository(new JdbcTemplate(db), Encryptors.queryableText("secret", "5b8bd7612cdab5ed"));				
		OAuthSessionManager sessionManager = new ConcurrentMapOAuthSessionManager(appRepository);
		AccountMapper accountMapper = new AccountMapper(new StubFileStorage(), "http://localhost:8080/members/{profileKey}");
		AccountRepository accountRepository = new JdbcAccountRepository(jdbcTemplate, NoOpPasswordEncoder.getInstance(), accountMapper);
		tokenServices = new OAuthSessionManagerProviderTokenServices(sessionManager, accountRepository, appRepository);
	}

	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}

	@Test
	public void oAuth10SessionLifecycle() throws InvalidRequestTokenException, NoSuchAccountConnectionException {
		executeOAuthSessionLifecycle(2);
	}

	private void executeOAuthSessionLifecycle(int numberOfTimes) throws InvalidRequestTokenException,
			NoSuchAccountConnectionException {
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

			Account account = new Account(1L, "Roy", "Clarkson", "rclarkson@vmware.com", "rlarkson", "http://localhost:8080/resources/profile-pics/1.jpg", new UriTemplate("http://localhost:8080/members/{id}"));
			Authentication auth = new UsernamePasswordAuthenticationToken(account, null, (Collection<GrantedAuthority>) null);
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
			assertEquals((Long) 1L, ((Account) auth2.getPrincipal()).getId());
		}
	}
}