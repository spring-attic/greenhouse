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
package com.springsource.greenhouse.config;

import org.joda.time.DateTimeZone;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodParameter;
import org.springframework.format.datetime.joda.JodaTimeContextHolder;
import org.springframework.mobile.device.mvc.DeviceWebArgumentResolver;
import org.springframework.security.core.Authentication;
import org.springframework.social.facebook.FacebookWebArgumentResolver;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.home.UserLocationHandlerInterceptor;
import com.springsource.greenhouse.utils.Location;

/**
 * A PostProcessor for Spring MVC's {@link AnnotationMethodHandlerAdapter}.
 * Performs additional configuration on the adapter instance required by the Greenhouse application.
 * Currently, this includes the registration of custom web argument resolvers.
 * 
 * TODO - see SPR-7327: it would be better to do this as part of instantiating AnnotationMethodHandlerAdapter.
 * Support in Spring MVC namespace should be added for that (this class would go away then)
 * 
 * @author Keith Donald
 */
public class AnnotationMethodHandlerAdapterPostProcessor implements BeanPostProcessor {

	public Object postProcessBeforeInitialization(Object bean, String name)
			throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
		if (bean instanceof AnnotationMethodHandlerAdapter) {
			AnnotationMethodHandlerAdapter controllerInvoker = (AnnotationMethodHandlerAdapter) bean;
			WebArgumentResolver[] resolvers = new WebArgumentResolver[5];
			resolvers[0] = new DeviceWebArgumentResolver();
			resolvers[1] = new AccountWebArgumentResolver();
			resolvers[2] = new FacebookWebArgumentResolver(facebookApiKey);
			resolvers[3] = new LocationWebArgumentResolver();
			resolvers[4] = new DateTimeZoneWebArgumentResolver();
			controllerInvoker.setCustomArgumentResolvers(resolvers);
		}
		return bean;
	}

	/**
	 * Resolves {@link Account} @Controller method parameter values from the current request's Authentication userPrincipal.
	 * @author Keith Donald
	 */
	private static class AccountWebArgumentResolver implements WebArgumentResolver {
		public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
			if (Account.class.isAssignableFrom(param.getParameterType())) {
				Authentication auth = (Authentication) request.getUserPrincipal();
				return auth != null && auth.getPrincipal() instanceof Account ? auth.getPrincipal() : null;
			} else {
				return WebArgumentResolver.UNRESOLVED;
			}
		}
	}

	/**
	 * Resolves {@link Location} @Controller method parameter values from the current request's resolved Location. 
	 * @author Keith Donald
	 */
	private static class LocationWebArgumentResolver implements WebArgumentResolver {
		public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
			if (Location.class.isAssignableFrom(param.getParameterType())) {
				return request.getAttribute(UserLocationHandlerInterceptor.USER_LOCATION_ATTRIBUTE, WebRequest.SCOPE_REQUEST);
			} else {
				return WebArgumentResolver.UNRESOLVED;
			}
		}
	}

	/**
	 * Resolves {@link DateTimeZone} @Controller method parameter values from the current request's resolved DateTimeZone. 
	 * @author Keith Donald
	 */
	private static class DateTimeZoneWebArgumentResolver implements WebArgumentResolver {
		public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
			if (DateTimeZone.class.isAssignableFrom(param.getParameterType())) {
				return JodaTimeContextHolder.getJodaTimeContext().getTimeZone();
			} else {
				return WebArgumentResolver.UNRESOLVED;
			}
		}
	}
	
	/**
	 * Needed by {@link FacebookWebArgumentResolver} to resolve Facebook cookies such as the user's Facebook ID and Greenhouse->Facebook access token. 
	 */
	@Value("#{facebookProvider.apiKey}")
	private String facebookApiKey;
	
}