package com.springsource.greenhouse.config;

import org.joda.time.DateTimeZone;
import org.springframework.core.MethodParameter;
import org.springframework.format.datetime.joda.JodaTimeContextHolder;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Resolves {@link DateTimeZone} @Controller method parameter values from the current request's resolved DateTimeZone. 
 * @author Keith Donald
 */
public class DateTimeZoneWebArgumentResolver implements WebArgumentResolver {
	public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
		if (DateTimeZone.class.isAssignableFrom(param.getParameterType())) {
			return JodaTimeContextHolder.getJodaTimeContext().getTimeZone();
		} else {
			return WebArgumentResolver.UNRESOLVED;
		}
	}
}