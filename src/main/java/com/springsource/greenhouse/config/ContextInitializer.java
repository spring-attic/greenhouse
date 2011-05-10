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

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class ContextInitializer implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {

	public void initialize(ConfigurableWebApplicationContext ctx) {
		ConfigurableEnvironment environment = ctx.getEnvironment();
		environment.setDefaultProfiles("embedded");
		if (environment.acceptsProfiles("embedded")) {
			Properties properties = new Properties();
			properties.put("application.url", "http://localhost:8080/greenhouse");
			properties.put("application.secureUrl", "http://localhost:8080/greenhouse");
			properties.put("application.secureChannel", "http");
			properties.put("facebook.appId", "21aa96c8bc23259d0dd2ab99e496c306");
			properties.put("facebook.appSecret", "f9f627194d471fb915dfbc856d347288");
			properties.put("twitter.consumerKey", "kqAm0GiPCT2owEtyTLPsug");
			properties.put("twitter.consumerSecret", "3461TFWV52VJuppKeaWMi8lKOxXMZtYLPGISq4nJ5s");
			properties.put("linkedin.consumerKey", "elcLPr8RxIXifjn5RKwaTNxKPap4dYrr9ANuJ-abZNkTjbT3mSOVT7IhSfsF27XP");
			properties.put("linkedin.consumerSecret", "QiMWSBRBBM43wFuqpn8XtXqdfLB6A0TJUQslBjtuQAwYCOcIdvRaotT9c50I72pk");
			properties.put("tripit.consumerKey", "739681c719f545d13d2376552d182b47c79b1d35");
			properties.put("tripit.consumerSecret", "b6287a592afb3eccefb9261ee87c96d3b5f39286");
			environment.getPropertySources().addFirst(new PropertiesPropertySource("properties", properties));
		} else if (environment.acceptsProfiles("standard")) {
			environment.getPropertySources().addFirst(new PropertiesPropertySource("properties", applicationProperties()));
		}
	}
	
	private Properties applicationProperties() {
		try {
			return PropertiesLoaderUtils.loadAllProperties("application.properties");
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}

