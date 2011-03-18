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

import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Feature;
import org.springframework.context.annotation.FeatureConfiguration;
import org.springframework.core.MethodParameter;
import org.springframework.format.datetime.joda.JodaTimeContextHolder;
import org.springframework.mobile.device.DeviceWebArgumentResolver;
import org.springframework.security.core.Authentication;
import org.springframework.social.facebook.web.FacebookWebArgumentResolver;
import org.springframework.validation.Validator;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.MvcAnnotationDriven;
import org.springframework.web.servlet.config.MvcResources;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.home.UserLocationHandlerInterceptor;
import com.springsource.greenhouse.utils.Location;

@FeatureConfiguration
public class MvcFeatureConfig {
	
	@Feature
	public MvcAnnotationDriven annotatedControllers(Validator validator, @Value("#{environment['facebook.appId']}") String facebookAppId,
			@Value("#{environment['facebook.appSecret']}") String facebookAppSecret) {
		return new MvcAnnotationDriven().validator(validator).
			argumentResolvers(new DeviceWebArgumentResolver(),
					new FacebookWebArgumentResolver(facebookAppId, facebookAppSecret),
					new AccountWebArgumentResolver(),
					new DateTimeZoneWebArgumentResolver(),
					new LocationWebArgumentResolver());
	}
	
	@Feature
	public MvcResources resourceHandler() {
		return new MvcResources("/resources/**", "/resources/");
	}

	static class AccountWebArgumentResolver implements WebArgumentResolver {
		public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
			if (Account.class.isAssignableFrom(param.getParameterType())) {
				Authentication auth = (Authentication) request.getUserPrincipal();
				return auth != null && auth.getPrincipal() instanceof Account ? auth.getPrincipal() : null;
			} else {
				return WebArgumentResolver.UNRESOLVED;
			}
		}
	}
	
	static class DateTimeZoneWebArgumentResolver implements WebArgumentResolver {
		public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
			if (DateTimeZone.class.isAssignableFrom(param.getParameterType())) {
				return JodaTimeContextHolder.getJodaTimeContext().getTimeZone();
			} else {
				return WebArgumentResolver.UNRESOLVED;
			}
		}
	}
	
	static class LocationWebArgumentResolver implements WebArgumentResolver {
		public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
			if (Location.class.isAssignableFrom(param.getParameterType())) {
				return request.getAttribute(UserLocationHandlerInterceptor.USER_LOCATION_ATTRIBUTE, WebRequest.SCOPE_REQUEST);
			} else {
				return WebArgumentResolver.UNRESOLVED;
			}
		}
	}
}
