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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.data.keyvalue.redis.core.StringRedisTemplate;
import org.springframework.data.keyvalue.redis.support.collections.DefaultRedisMap;
import org.springframework.data.keyvalue.redis.support.collections.RedisMap;

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
		RedisMap<String, String> sessionHash = sessionHash(session.getRequestToken());
		sessionHash.putAll(toHash(session));
		sessionHash.expire(2, TimeUnit.MINUTES);
	}

	@Override
	protected StandardOAuthSession get(String requestToken) {
		return fromHash(requestToken, sessionHash(requestToken));
	}

	@Override
	protected void remove(String requestToken) {
		redisTemplate.delete(key(requestToken));
	}
	
	// internal helpers

	private RedisMap<String, String> sessionHash(String requestToken) {
		return new DefaultRedisMap<String, String>(key(requestToken), redisTemplate);
	}

	private String key(String requestToken) {
		return "oauthSession:" + requestToken;
	}

	private Map<String, String> toHash(StandardOAuthSession session) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("apiKey", session.getApiKey());
		map.put("callbackUrl", session.getCallbackUrl());
		map.put("secret", session.getSecret());
		map.put("authorizingAccountId", session.getAuthorizingAccountId().toString());
		map.put("verifer", session.getVerifier());
		return map;
	}

	private StandardOAuthSession fromHash(String requestToken, Map<String, String> hash) {
		return new StandardOAuthSession(hash.get("apiKey"), hash.get("callbackUrl"), requestToken, hash.get("secret"), Long.valueOf(hash.get("authorizingAccountId")), hash.get("verifier"));
	}

}