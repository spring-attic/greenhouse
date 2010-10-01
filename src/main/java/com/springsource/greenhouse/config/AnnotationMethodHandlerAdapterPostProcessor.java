package com.springsource.greenhouse.config;

import org.joda.time.DateTimeZone;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodParameter;
import org.springframework.format.datetime.joda.JodaTimeContextHolder;
import org.springframework.mobile.mvc.DeviceWebArgumentResolver;
import org.springframework.security.core.Authentication;
import org.springframework.social.facebook.FacebookWebArgumentResolver;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.action.Location;
import com.springsource.greenhouse.home.UserLocationHandlerInterceptor;

// TODO - see SPR-7327: it would be better to do this as part of instantiating AnnotationMethodHandlerAdapter
// support in Spring MVC namespace should be added for that (this would go away then)
public class AnnotationMethodHandlerAdapterPostProcessor implements BeanPostProcessor {

	@Value("#{facebookAccountProvider.apiKey}")
	private String facebookAppKey;
	
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
			resolvers[2] = new FacebookWebArgumentResolver(facebookAppKey);
			resolvers[3] = new LocationWebArgumentResolver();
			resolvers[4] = new DateTimeZoneWebArgumentResolver();
			controllerInvoker.setCustomArgumentResolvers(resolvers);
		}
		return bean;
	}

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
	
	private static class LocationWebArgumentResolver implements WebArgumentResolver {
		public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
			if (Location.class.isAssignableFrom(param.getParameterType())) {
				return request.getAttribute(UserLocationHandlerInterceptor.USER_LOCATION_ATTRIBUTE, WebRequest.SCOPE_REQUEST);
			} else {
				return WebArgumentResolver.UNRESOLVED;
			}
		}
	}

	private static class DateTimeZoneWebArgumentResolver implements WebArgumentResolver {
		public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
			if (DateTimeZone.class.isAssignableFrom(param.getParameterType())) {
				return JodaTimeContextHolder.getJodaTimeContext().getTimeZone();
			} else {
				return WebArgumentResolver.UNRESOLVED;
			}
		}
	}
}