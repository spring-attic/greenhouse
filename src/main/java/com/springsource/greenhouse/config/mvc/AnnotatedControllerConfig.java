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
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTimeZone;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.format.datetime.joda.JodaTimeContextHolder;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.social.facebook.web.FacebookHandlerMethodArgumentResolver;
import org.springframework.social.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.home.DateTimeZoneHandlerInterceptor;
import com.springsource.greenhouse.home.UserLocationHandlerInterceptor;
import com.springsource.greenhouse.signin.AccountExposingHandlerInterceptor;
import com.springsource.greenhouse.utils.Location;

@Configuration
public class AnnotatedControllerConfig {
	
	@Inject
	private ConfigurableWebApplicationContext context;

	@Bean
	public HandlerMapping annotatedControllerMapping() {
		RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
		handlerMapping.setInterceptors(interceptors());
		handlerMapping.setOrder(1);
		return handlerMapping;
	}

	@Bean
	public RequestMappingHandlerAdapter annotatedControllerAdapter() {
		RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();

		ConfigurableWebBindingInitializer bindingInitializer = new ConfigurableWebBindingInitializer();
		bindingInitializer.setConversionService(conversionService());
		bindingInitializer.setValidator(validator());
		handlerAdapter.setWebBindingInitializer(bindingInitializer);
		
		handlerAdapter.setMessageConverters(messageConverters());
		handlerAdapter.setCustomArgumentResolvers(customArgumentResolvers());
		return handlerAdapter;
	}

	public ConversionService conversionService() {
		return new DefaultFormattingConversionService();
	}
	
	@Bean
	public Validator validator() {
		LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("/WEB-INF/messages/validation");
		if (getEnvironment().acceptsProfiles("embedded")) {
			messageSource.setCacheSeconds(0);
		}
		factory.setValidationMessageSource(messageSource);
		return factory;
	}

	// subclassing hooks
	
	protected List<HttpMessageConverter<?>> messageConverters() {
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(1);
		converters.add(new MappingJacksonHttpMessageConverter());
		return converters;
	}

	protected List<HandlerMethodArgumentResolver> customArgumentResolvers() {
		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
		argumentResolvers.add(new AccountHandlerMethodArgumentResolver());
		argumentResolvers.add(new DateTimeZoneHandlerMethodArgumentResolver());
		argumentResolvers.add(new LocationHandlerMethodArgumentResolver());
		argumentResolvers.add(new FacebookHandlerMethodArgumentResolver(getEnvironment().getProperty("facebook.appId"), getEnvironment().getProperty("facebook.appSecret")));
		argumentResolvers.add(new DeviceHandlerMethodArgumentResolver());
		return argumentResolvers;
	}
	
	// internal helpers

	private HandlerInterceptor[] interceptors() {
		return new HandlerInterceptor[] {
			new ConversionServiceExposingInterceptor(conversionService()),
			new AccountExposingHandlerInterceptor(),
			new DateTimeZoneHandlerInterceptor(),
			new UserLocationHandlerInterceptor(),
			new DeviceResolverHandlerInterceptor()
		};
	}

	private Environment getEnvironment() {
		return context.getEnvironment();
	}
	
	static class AccountHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

		public boolean supportsParameter(MethodParameter parameter) {
			return Account.class.isAssignableFrom(parameter.getParameterType());
		}

		public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest,
				WebDataBinderFactory binderFactory) throws Exception {
			Authentication auth = (Authentication) webRequest.getUserPrincipal();
			return auth != null && auth.getPrincipal() instanceof Account ? auth.getPrincipal() : null;
		}

	}
	
	static class DateTimeZoneHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

		public boolean supportsParameter(MethodParameter parameter) {
			return DateTimeZone.class.isAssignableFrom(parameter.getParameterType());
		}

		public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest,
				WebDataBinderFactory binderFactory) throws Exception {
			return JodaTimeContextHolder.getJodaTimeContext().getTimeZone();
		}
		
	}
	
	static class LocationHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

		public boolean supportsParameter(MethodParameter parameter) {
			return Location.class.isAssignableFrom(parameter.getParameterType());
		}

		public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest,
				WebDataBinderFactory binderFactory) throws Exception {
			return webRequest.getAttribute(UserLocationHandlerInterceptor.USER_LOCATION_ATTRIBUTE, WebRequest.SCOPE_REQUEST);
		}
		
	}
	
}