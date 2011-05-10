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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.joda.time.DateTimeZone;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;
import org.springframework.web.servlet.view.tiles2.TilesView;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.home.DateTimeZoneHandlerInterceptor;
import com.springsource.greenhouse.home.UserLocationHandlerInterceptor;
import com.springsource.greenhouse.signin.AccountExposingHandlerInterceptor;
import com.springsource.greenhouse.utils.Location;

@Configuration
public class WebConfig {

	@Inject
	private WebApplicationContext context;

	@Inject
	private Environment environment;

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
		if (environment.acceptsProfiles("embedded")) {
			messageSource.setCacheSeconds(0);
		}
		factory.setValidationMessageSource(messageSource);
		return factory;
	}

	// resource handling
	
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
	
	@Bean
	public ViewResolver viewResolver() {
		UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
		viewResolver.setViewClass(TilesView.class);
		return viewResolver;
	}
	
	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer configurer = new TilesConfigurer();
		configurer.setDefinitions(new String[] {
			"/WEB-INF/layouts/tiles.xml",
			"/WEB-INF/views/**/tiles.xml"				
		});
		configurer.setCheckRefresh(true);
		return configurer;
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("/WEB-INF/messages/messages");
		if (environment.acceptsProfiles("embedded")) {
			messageSource.setCacheSeconds(0);
		}
		return messageSource;
	}
	
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(500000);
		return multipartResolver;
	}
	
	// internal helpers

	private List<HttpMessageConverter<?>> messageConverters() {
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(1);
		converters.add(new MappingJacksonHttpMessageConverter());
		return converters;
	}

	private List<HandlerMethodArgumentResolver> customArgumentResolvers() {
		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
		argumentResolvers.add(new AccountHandlerMethodArgumentResolver());
		argumentResolvers.add(new DateTimeZoneHandlerMethodArgumentResolver());
		argumentResolvers.add(new LocationHandlerMethodArgumentResolver());
		argumentResolvers.add(new FacebookHandlerMethodArgumentResolver(environment.getProperty("facebook.appId"), environment.getProperty("facebook.appSecret")));
		argumentResolvers.add(new DeviceHandlerMethodArgumentResolver());
		return argumentResolvers;
	}
	
	private HandlerInterceptor[] interceptors() {
		return new HandlerInterceptor[] {
			new ConversionServiceExposingInterceptor(conversionService()),
			new AccountExposingHandlerInterceptor(),
			new DateTimeZoneHandlerInterceptor(),
			new UserLocationHandlerInterceptor(),
			new DeviceResolverHandlerInterceptor()
		};
	}
	
	private static class AccountHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

		public boolean supportsParameter(MethodParameter parameter) {
			return Account.class.isAssignableFrom(parameter.getParameterType());
		}

		public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest,
				WebDataBinderFactory binderFactory) throws Exception {
			Authentication auth = (Authentication) webRequest.getUserPrincipal();
			return auth != null && auth.getPrincipal() instanceof Account ? auth.getPrincipal() : null;
		}

	}
	
	private static class DateTimeZoneHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

		public boolean supportsParameter(MethodParameter parameter) {
			return DateTimeZone.class.isAssignableFrom(parameter.getParameterType());
		}

		public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest,
				WebDataBinderFactory binderFactory) throws Exception {
			return JodaTimeContextHolder.getJodaTimeContext().getTimeZone();
		}
		
	}
	
	private static class LocationHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

		public boolean supportsParameter(MethodParameter parameter) {
			return Location.class.isAssignableFrom(parameter.getParameterType());
		}

		public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest,
				WebDataBinderFactory binderFactory) throws Exception {
			return webRequest.getAttribute(UserLocationHandlerInterceptor.USER_LOCATION_ATTRIBUTE, WebRequest.SCOPE_REQUEST);
		}
		
	}

}