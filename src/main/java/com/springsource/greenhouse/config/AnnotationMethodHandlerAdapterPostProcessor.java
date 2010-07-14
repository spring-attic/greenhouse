package com.springsource.greenhouse.config;

import javax.inject.Inject;

import net.sourceforge.wurfl.core.Device;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodParameter;
import org.springframework.mobile.DeviceDetectingHandlerInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;
import org.springframework.security.oauth.extras.OAuthConsumerAccessTokenWebArgumentResolver;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.springsource.greenhouse.account.Account;

// TODO - see SPR-7327: it would be better to do this as part of instantiating AnnotationMethodHandlerAdapter
// support in Spring MVC namespace should be added for that (this would go away then)
public class AnnotationMethodHandlerAdapterPostProcessor implements BeanPostProcessor {

	private OAuthConsumerTokenServicesFactory oauthTokenFactory;
	
	@Inject
	public AnnotationMethodHandlerAdapterPostProcessor(OAuthConsumerTokenServicesFactory oauthTokenFactory) {
		this.oauthTokenFactory = oauthTokenFactory;
	}

	public Object postProcessBeforeInitialization(Object bean, String name)
			throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
		if (bean instanceof AnnotationMethodHandlerAdapter) {
			AnnotationMethodHandlerAdapter controllerInvoker = (AnnotationMethodHandlerAdapter) bean;
			WebArgumentResolver[] resolvers = new WebArgumentResolver[3];
			resolvers[0] = new DeviceWebArgumentResolver();
			resolvers[1] = new GreenhouseUserDetailsWebArgumentResolver();
			resolvers[2] = new OAuthConsumerAccessTokenWebArgumentResolver(oauthTokenFactory);
			controllerInvoker.setCustomArgumentResolvers(resolvers);
		}
		return bean;
	}

	private static class DeviceWebArgumentResolver implements WebArgumentResolver {
		public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
			if (Device.class.isAssignableFrom(param.getParameterType())) {
				return request.getAttribute(DeviceDetectingHandlerInterceptor.DEVICE_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
			} else {
				return WebArgumentResolver.UNRESOLVED;
			}
		}
	}
	
	private static class GreenhouseUserDetailsWebArgumentResolver implements WebArgumentResolver {
		public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
			if (Account.class.isAssignableFrom(param.getParameterType())) {
				Authentication auth = (Authentication) request.getUserPrincipal();
				return auth != null && auth.getPrincipal() instanceof Account ? auth.getPrincipal() : null;
			} else {
				return WebArgumentResolver.UNRESOLVED;
			}
		}
	}

}