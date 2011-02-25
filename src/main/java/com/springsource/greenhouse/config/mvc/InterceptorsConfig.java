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
package com.springsource.greenhouse.config.mvc;

import org.springframework.context.annotation.Feature;
import org.springframework.context.annotation.FeatureConfiguration;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.servlet.config.MvcInterceptors;

import com.springsource.greenhouse.home.DateTimeZoneHandlerInterceptor;
import com.springsource.greenhouse.home.UserLocationHandlerInterceptor;
import com.springsource.greenhouse.signin.AccountExposingHandlerInterceptor;

@FeatureConfiguration
public class InterceptorsConfig {

	@Feature
	public MvcInterceptors interceptors() {
		return new MvcInterceptors().globalInterceptors(new AccountExposingHandlerInterceptor(),
				new DateTimeZoneHandlerInterceptor(),
				new UserLocationHandlerInterceptor(),
				new DeviceResolverHandlerInterceptor());
	}
	
}
