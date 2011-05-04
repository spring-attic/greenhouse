/*
 * Copyright 2011 the original author or authors.
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
package com.springsource.greenhouse.config.mvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Configuration
public class ResourceHandlerConfig {

	@Inject
	private WebApplicationContext context;

	@Bean
	public HandlerMapping resourceHttpRequestHandlerMapping() {
		SimpleUrlHandlerMapping resourceMapping = new SimpleUrlHandlerMapping();
		Map<String, ResourceHttpRequestHandler> urlMap = Collections.singletonMap("/resources/**", resourceHttpRequestHandler());
		resourceMapping.setUrlMap(urlMap);
		resourceMapping.setOrder(0);
		return resourceMapping;
	}
	
	@Bean
	public ResourceHttpRequestHandler resourceHttpRequestHandler() {
		ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
		List<Resource> locations = new ArrayList<Resource>(1);
		locations.add(context.getResource("/resources/"));
		handler.setLocations(locations);
		return handler;
	}

	@Bean
	public HttpRequestHandlerAdapter httpRequestHandlerAdapter() {
		return new HttpRequestHandlerAdapter();
	}
	
}