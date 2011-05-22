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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.data.keyvalue.redis.core.BoundHashOperations;
import org.springframework.data.keyvalue.redis.core.StringRedisTemplate;

import com.springsource.greenhouse.develop.AppRepository;

/**
 * OAuthSessionManager implementation that stores OAuthSessions in the Redis key-value store.
 * @author Keith Donald
 */
public class RedisOAuthSessionManager extends AbstractOAuthSessionManager {

	private final StringRedisTemplate redisTemplate;
	
	@Inject
	public RedisOAuthSessionManager(StringRedisTemplate redisTemplate, AppRepository appRepository) {
		super(appRepository);
		this.redisTemplate = redisTemplate;
	}

	@Override
	protected void put(StandardOAuthSession session) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key(session.getRequestToken()));
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("apiKey", session.getApiKey());
		data.put("callbackUrl", session.getCallbackUrl());
		data.put("secret", session.getSecret());		
		hashOps.putAll(data);
		hashOps.expire(2, TimeUnit.MINUTES);
	}

	@Override
	protected StandardOAuthSession get(String requestToken) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key(requestToken));
		Collection<String> values = hashOps.multiGet(Arrays.asList("apiKey", "callbackUrl", "secret", "authorizingAccountId", "verifier"));
		Iterator<String> it = values.iterator();
		String apiKey = it.next();
		if (apiKey == null) {
			return null;
		}
		return new StandardOAuthSession(apiKey, it.next(), requestToken, it.next(), toLong(it.next()), it.next());
	}

	@Override
	protected void authorized(StandardOAuthSession session) {
		BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key(session.getRequestToken()));
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("authorizingAccountId", session.getAuthorizingAccountId().toString());
		data.put("verifer", session.getVerifier());
		hashOps.putAll(data);		
	}
	
	@Override
	protected void remove(String requestToken) {
		redisTemplate.delete(key(requestToken));
	}
	
	// internal helpers

	private String key(String requestToken) {
		return "oauthSession:" + requestToken;
	}
	
	private Long toLong(String value) {
		return value != null ? Long.valueOf(value) : null;
	}

}