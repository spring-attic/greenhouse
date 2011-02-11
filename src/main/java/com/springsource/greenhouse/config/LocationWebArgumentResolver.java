package com.springsource.greenhouse.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.springsource.greenhouse.home.UserLocationHandlerInterceptor;
import com.springsource.greenhouse.utils.Location;

/**
 * Resolves {@link Location} @Controller method parameter values from the current request's resolved Location. 
 * @author Keith Donald
 */
public class LocationWebArgumentResolver implements WebArgumentResolver {
	public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
		if (Location.class.isAssignableFrom(param.getParameterType())) {
			return request.getAttribute(UserLocationHandlerInterceptor.USER_LOCATION_ATTRIBUTE, WebRequest.SCOPE_REQUEST);
		} else {
			return WebArgumentResolver.UNRESOLVED;
		}
	}
}