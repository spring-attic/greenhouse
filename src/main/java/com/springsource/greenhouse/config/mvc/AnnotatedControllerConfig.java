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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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
import org.springframework.web.method.annotation.support.ErrorsMethodArgumentResolver;
import org.springframework.web.method.annotation.support.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.support.ModelMethodProcessor;
import org.springframework.web.method.annotation.support.RequestHeaderMapMethodArgumentResolver;
import org.springframework.web.method.annotation.support.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.support.RequestParamMapMethodArgumentResolver;
import org.springframework.web.method.annotation.support.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMethodAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMethodExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.annotation.support.HttpEntityMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.support.PathVariableMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.support.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.support.ServletCookieValueMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.support.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.support.ServletRequestMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.support.ServletResponseMethodArgumentResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

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
		RequestMappingHandlerMethodMapping handlerMapping = new RequestMappingHandlerMethodMapping();
		handlerMapping.setMappedInterceptors(mappedInterceptors());
		handlerMapping.setOrder(1);
		return handlerMapping;
	}

	@Bean
	public RequestMappingHandlerMethodAdapter annotatedControllerAdapter() {
		RequestMappingHandlerMethodAdapter handlerAdapter = new RequestMappingHandlerMethodAdapter();

		ConfigurableWebBindingInitializer bindingInitializer = new ConfigurableWebBindingInitializer();
		bindingInitializer.setConversionService(conversionService());
		bindingInitializer.setValidator(validator());
		handlerAdapter.setWebBindingInitializer(bindingInitializer);
		
		handlerAdapter.setMessageConverters(messageConverters());
		handlerAdapter.setHandlerMethodArgumentResolvers(handlerMethodArgumentResolvers());
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

	// interception-related
	 
	@Bean
	public HandlerExceptionResolver annotatedControllerExceptionResolver() {
		RequestMappingHandlerMethodExceptionResolver exceptionResolver = new RequestMappingHandlerMethodExceptionResolver();
		exceptionResolver.setMessageConverters(messageConverters());
		exceptionResolver.setOrder(0);
		return exceptionResolver;
	}

	@Bean
	public HandlerExceptionResolver responseStatusExceptionResolver() {
		ResponseStatusExceptionResolver exceptionResolver = new ResponseStatusExceptionResolver();
		exceptionResolver.setOrder(1);
		return exceptionResolver;
	}

	@Bean
	public HandlerExceptionResolver defaultHandlerExceptionResolver() {
		DefaultHandlerExceptionResolver exceptionResolver = new DefaultHandlerExceptionResolver();
		exceptionResolver.setOrder(2);
		return exceptionResolver;
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(500000);
		return multipartResolver;
	}
	
	// subclassing hooks
	
	protected HttpMessageConverter<?>[] messageConverters() {
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(1);
		converters.add(new MappingJacksonHttpMessageConverter());
		return converters.toArray(new HttpMessageConverter<?>[converters.size()]);
	}

	protected HandlerMethodArgumentResolver[] handlerMethodArgumentResolvers() {
		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
		addCustomHandlerMethodResolvers(argumentResolvers);
		addStandardHandlerMethodResolvers(argumentResolvers);
		return argumentResolvers.toArray(new HandlerMethodArgumentResolver[argumentResolvers.size()]);
	}
	
	protected void addCustomHandlerMethodResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new AccountHandlerMethodArgumentResolver());
		argumentResolvers.add(new DateTimeZoneHandlerMethodArgumentResolver());
		argumentResolvers.add(new LocationHandlerMethodArgumentResolver());
		argumentResolvers.add(new FacebookHandlerMethodArgumentResolver(getEnvironment().getProperty("facebook.appId"),
				getEnvironment().getProperty("facebook.appSecret")));
		argumentResolvers.add(new DeviceHandlerMethodArgumentResolver());
	}

	// internal helpers

	private MappedInterceptor[] mappedInterceptors() {
		return new MappedInterceptor[] {
			new MappedInterceptor(null, new ConversionServiceExposingInterceptor(conversionService())),
			new MappedInterceptor(null, new AccountExposingHandlerInterceptor()),
			new MappedInterceptor(null, new DateTimeZoneHandlerInterceptor()),
			new MappedInterceptor(null, new UserLocationHandlerInterceptor()),
			new MappedInterceptor(null, new DeviceResolverHandlerInterceptor())
		};
	}
	
	private void addStandardHandlerMethodResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		// Annotation-based resolvers
		argumentResolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
		argumentResolvers.add(new RequestParamMapMethodArgumentResolver());
		argumentResolvers.add(new PathVariableMethodArgumentResolver(getBeanFactory()));
		argumentResolvers.add(new ServletModelAttributeMethodProcessor(false));
		argumentResolvers.add(new RequestResponseBodyMethodProcessor(messageConverters()));
		argumentResolvers.add(new RequestHeaderMethodArgumentResolver(getBeanFactory()));
		argumentResolvers.add(new RequestHeaderMapMethodArgumentResolver());
		argumentResolvers.add(new ServletCookieValueMethodArgumentResolver(getBeanFactory()));
		argumentResolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));

		// Type-based resolvers
		argumentResolvers.add(new ServletRequestMethodArgumentResolver());
		argumentResolvers.add(new ServletResponseMethodArgumentResolver());
		argumentResolvers.add(new HttpEntityMethodProcessor(messageConverters()));
		argumentResolvers.add(new ModelMethodProcessor());
		argumentResolvers.add(new ErrorsMethodArgumentResolver());
		
		// Default-mode resolution
		argumentResolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), true));
		argumentResolvers.add(new ServletModelAttributeMethodProcessor(true));
	}

	private ConfigurableBeanFactory getBeanFactory() {
		return context.getBeanFactory();
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