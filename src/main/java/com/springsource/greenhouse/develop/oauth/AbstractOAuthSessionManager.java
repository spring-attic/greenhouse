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

import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import com.springsource.greenhouse.develop.AppConnection;
import com.springsource.greenhouse.develop.AppRepository;
import com.springsource.greenhouse.develop.InvalidApiKeyException;

/**
 * Base OAuthSessionManager implementation independent of a specific session storage strategy.
 * @author Keith Donald
 */
public abstract class AbstractOAuthSessionManager implements OAuthSessionManager {

	private final AppRepository appRepository;
	
	private final StringKeyGenerator keyGenerator = KeyGenerators.string();

	public AbstractOAuthSessionManager(AppRepository appRepository) {
		this.appRepository = appRepository;
	}
	
	public OAuthSession newOAuthSession(String apiKey, String callbackUrl) {
		StandardOAuthSession session = new StandardOAuthSession(apiKey, callbackUrl, keyGenerator.generateKey(), keyGenerator.generateKey());
		put(session);
		return session;
	}

	public OAuthSession getSession(String requestToken) throws InvalidRequestTokenException {
		OAuthSession session = get(requestToken);
		if (session == null) {
			throw new InvalidRequestTokenException(requestToken);
		}
		return session;
	}

	public OAuthSession authorize(String requestToken, Long authorizingAccountId, String verifier) throws InvalidRequestTokenException {
		StandardOAuthSession session = get(requestToken);
		if (session.authorized()) {
			throw new IllegalStateException("OAuthSession is already authorized");
		}
		session.authorize(authorizingAccountId, verifier);
		return session;
	}

	public AppConnection grantAccess(String requestToken) throws InvalidRequestTokenException {
		StandardOAuthSession session = get(requestToken);
		if (!session.authorized()) {
			throw new IllegalStateException("OAuthSession is not yet authorized");
		}
		try {
			AppConnection connection = appRepository.connectApp(session.getAuthorizingAccountId(), session.getApiKey());
			remove(requestToken);
			return connection;
		} catch (InvalidApiKeyException e) {
			throw new IllegalStateException("Unable to grant access due to session - have the App's key changed?", e);
		}
	}
	
	// subclassing hooks

	protected abstract void put(StandardOAuthSession session);

	protected abstract StandardOAuthSession get(String requestToken);

	protected abstract void remove(String requestToken);
	
}