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

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration for Greenhouse application @Components such as @Services, @Repositories, and @Controllers.
 * Loads externalized property values required to configure the various application properties.
 * Not much else here, as we rely on @Component scanning in conjunction with @Inject by-type autowiring.
 * @author Keith Donald
 */
@Configuration
@ComponentScan(basePackages="com.springsource.greenhouse")
public class ComponentConfig {
	
	/**
	 * Properties to support the 'embedded' mode of operation.
	 */
	@Configuration
	@Profile("embedded")
	@PropertySource("classpath:com/springsource/greenhouse/config/embedded.properties")
	static class Embedded {
	}

	/**
	 * Properties to support the 'standard' mode of operation.
	 */
	@Configuration
	@Profile("standard")
	@PropertySource("classpath:application.properties")
	static class Standard {
	}

}