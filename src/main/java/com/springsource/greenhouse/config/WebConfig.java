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

import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTimeZone;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.format.datetime.joda.JodaTimeContextHolder;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.social.facebook.web.FacebookHandlerMethodArgumentResolver;
import org.springframework.social.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;
import org.springframework.web.servlet.view.tiles2.TilesView;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.home.DateTimeZoneHandlerInterceptor;
import com.springsource.greenhouse.home.UserLocationHandlerInterceptor;
import com.springsource.greenhouse.signin.AccountExposingHandlerInterceptor;
import com.springsource.greenhouse.utils.Location;

/**
 * Spring MVC Configuration.
 * Implements {@link WebMvcConfigurer}, which provides convenient callbacks that allow us to customize aspects of the Spring Web MVC framework.
 * These callbacks allow us to register custom interceptors, message converters, argument resovlers, a validator, resource handling, and other things.
 * @author Keith Donald
 * @see WebMvcConfigurer
 */
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

	@Inject
	private Environment environment;

	// implementing WebMvcConfigurer

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AccountExposingHandlerInterceptor());
		registry.addInterceptor(new DateTimeZoneHandlerInterceptor());
		registry.addInterceptor(new UserLocationHandlerInterceptor());
		registry.addInterceptor(new DeviceResolverHandlerInterceptor());
	}

	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new AccountHandlerMethodArgumentResolver());
		argumentResolvers.add(new DateTimeZoneHandlerMethodArgumentResolver());
		argumentResolvers.add(new LocationHandlerMethodArgumentResolver());
		argumentResolvers.add(new FacebookHandlerMethodArgumentResolver(environment.getProperty("facebook.appId"), environment.getProperty("facebook.appSecret")));
		argumentResolvers.add(new DeviceHandlerMethodArgumentResolver());		
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
	
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJacksonHttpMessageConverter());
	}
	
	public Validator getValidator() {
		LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("/WEB-INF/messages/validation");
		if (environment.acceptsProfiles("embedded")) {
			messageSource.setCacheSeconds(0);
		}
		factory.setValidationMessageSource(messageSource);
		return factory;
	}

	// additional webmvc-related beans

	/**
	 * ViewResolver configuration required to work with Tiles2-based views.
	 */
	@Bean
	public ViewResolver viewResolver() {
		UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
		viewResolver.setViewClass(TilesView.class);
		return viewResolver;
	}

	/**
	 * Configures Tiles at application startup.
	 */
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
	
	/**
	 * Messages to support internationalization/localization.
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("/WEB-INF/messages/messages");
		if (environment.acceptsProfiles("embedded")) {
			messageSource.setCacheSeconds(0);
		}
		return messageSource;
	}

	/**
	 * Supports FileUploads.
	 */
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(500000);
		return multipartResolver;
	}
	
	// custom argument resolver inner classes

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