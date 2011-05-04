/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.develop.oauth;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Service;

import com.google.common.collect.MapMaker;
import com.springsource.greenhouse.develop.AppConnection;
import com.springsource.greenhouse.develop.AppRepository;
import com.springsource.greenhouse.develop.InvalidApiKeyException;

/**
 * OAuthSessionManager implementation that stores OAuthSessions in an in-memory ConcurrentMap.
 * Depends on an AppRepository to store app connections.
 * The ConcurrentMap, bult by Google Guava's MapMaker, uses soft values and expires entries after 2 minutes of idle activity.
 * @author Keith Donald
 * @see MapMaker
 */
@Service
public class ConcurrentMapOAuthSessionManager implements OAuthSessionManager {

	private final ConcurrentMap<String, StandardOAuthSession> sessions;

	private final AppRepository appRepository;
	
	private final StringKeyGenerator keyGenerator = KeyGenerators.string();

	@Inject
	public ConcurrentMapOAuthSessionManager(AppRepository appRepository) {
		this.appRepository = appRepository;
		sessions = new MapMaker().softValues().expireAfterWrite(2, TimeUnit.MINUTES).makeMap();
	}
	
	public OAuthSession newOAuthSession(String apiKey, String callbackUrl) {
		StandardOAuthSession session = new StandardOAuthSession(apiKey, callbackUrl, keyGenerator.generateKey(), keyGenerator.generateKey());
		sessions.put(session.getRequestToken(), session);
		return session;
	}

	public OAuthSession getSession(String requestToken) throws InvalidRequestTokenException {
		OAuthSession session = sessions.get(requestToken);
		if (session == null) {
			throw new InvalidRequestTokenException(requestToken);
		}
		return session;
	}

	public OAuthSession authorize(String requestToken, Long authorizingAccountId, String verifier) throws InvalidRequestTokenException {
		StandardOAuthSession session = getStandardSession(requestToken);
		if (session.authorized()) {
			throw new IllegalStateException("OAuthSession is already authorized");
		}
		session.authorize(authorizingAccountId, verifier);
		return session;
	}

	public AppConnection grantAccess(String requestToken) throws InvalidRequestTokenException {
		StandardOAuthSession session = getStandardSession(requestToken);
		if (!session.authorized()) {
			throw new IllegalStateException("OAuthSession is not yet authorized");
		}
		try {
			AppConnection connection = appRepository.connectApp(session.getAuthorizingAccountId(), session.getApiKey());
			sessions.remove(requestToken);
			return connection;
		} catch (InvalidApiKeyException e) {
			throw new IllegalStateException("Unable to grant access due to session - have the App's key changed?", e);
		}
	}
	
	// internal helpers
	
	private StandardOAuthSession getStandardSession(String requestToken) throws InvalidRequestTokenException {
		return (StandardOAuthSession) getSession(requestToken);
	}
	
	private static class StandardOAuthSession implements OAuthSession {

		private String apiKey;
		
		private String callbackUrl;
		
		private String requestToken;
		
		private String secret;
		
		private Long authorizingAccountId;
		
		private String verifier;
		
		public StandardOAuthSession(String apiKey, String callbackUrl, String requestToken, String secret) {
			this.apiKey = apiKey;
			this.callbackUrl = callbackUrl;
			this.requestToken = requestToken;
			this.secret = secret;
		}

		public String getApiKey() {
			return apiKey;
		}

		public String getCallbackUrl() {
			return callbackUrl;
		}

		public String getRequestToken() {
			return requestToken;
		}

		public String getSecret() {
			return secret;
		}

		public void authorize(Long authorizingAccountId, String verifier) {
			this.authorizingAccountId = authorizingAccountId;
			this.verifier = verifier;
		}

		public boolean authorized() {
			return authorizingAccountId != null;
		}

		public Long getAuthorizingAccountId() {
			return authorizingAccountId;
		}

		public String getVerifier() {
			return verifier;
		}

	}

}