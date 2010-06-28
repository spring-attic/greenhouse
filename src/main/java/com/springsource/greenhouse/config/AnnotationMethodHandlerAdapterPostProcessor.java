package com.springsource.greenhouse.config;

import javax.inject.Inject;

import net.sourceforge.wurfl.core.Device;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.mobile.DeviceDetectingHandlerInterceptor;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

// TODO - it would be better to do this as part of instantiating AnnotationMethodHandlerAdapter
// TODO - support in Spring MVC namespace should be added for that (this would go away then)
public class AnnotationMethodHandlerAdapterPostProcessor implements BeanPostProcessor {

	private AnnotationMethodHandlerAdapter controllerInvoker;

	@Inject
	public AnnotationMethodHandlerAdapterPostProcessor(AnnotationMethodHandlerAdapter controllerInvoker) {
		this.controllerInvoker = controllerInvoker;
	}

	public Object postProcessBeforeInitialization(Object bean, String name)
			throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String name)
			throws BeansException {
		controllerInvoker.setCustomArgumentResolver(new DeviceWebArgumentResolver());
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

}