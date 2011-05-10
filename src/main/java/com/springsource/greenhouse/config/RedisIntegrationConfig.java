/*
 * Copyright 2010-2011 the original author or authors.
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
package com.springsource.greenhouse.config;

import java.util.HashMap;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.service.keyvalue.RedisServiceCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.keyvalue.redis.connection.RedisConnectionFactory;
import org.springframework.data.keyvalue.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.integration.json.JsonInboundMessageMapper;
import org.springframework.integration.json.JsonOutboundMessageMapper;
import org.springframework.integration.support.converter.MessageConverter;
import org.springframework.integration.support.converter.SimpleMessageConverter;
import org.springframework.util.CollectionUtils;

@Configuration
@Profile("standard")
public class RedisIntegrationConfig {

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		if (CollectionUtils.isEmpty(cloudEnvironment().getServices())) {
			return new JedisConnectionFactory();
		} else {
			return redisServiceCreator().createSingletonService().service;
		}
	}
	
	@Bean
	public CloudEnvironment cloudEnvironment() {
		return new CloudEnvironment();
	}
	
	@Bean
	public RedisServiceCreator redisServiceCreator() {
		return new RedisServiceCreator(cloudEnvironment());
	}
	
	@Bean
	public MessageConverter jsonConverter() {
		SimpleMessageConverter converter = new SimpleMessageConverter();
		JsonInboundMessageMapper inbound = new JsonInboundMessageMapper(HashMap.class);
		inbound.setMapToPayload(true);
		JsonOutboundMessageMapper outbound = new JsonOutboundMessageMapper();
		outbound.setShouldExtractPayload(true);
		converter.setInboundMessageMapper(inbound);
		converter.setOutboundMessageMapper(outbound);		
		return converter;
	}
}
