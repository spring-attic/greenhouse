package org.springframework.social.facebook;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

public class FacebookUserWebArgumentResolver implements WebArgumentResolver {

	private final String applicationKey;

	public FacebookUserWebArgumentResolver(String applicationKey) {
		this.applicationKey = applicationKey;
	}
	
	public Object resolveArgument(MethodParameter parameter, NativeWebRequest request) throws Exception {
		FacebookUserId userIdAnnotation = parameter.getParameterAnnotation(FacebookUserId.class);
		if (userIdAnnotation != null) {
			HttpServletRequest nativeRequest = (HttpServletRequest) request.getNativeRequest();
			Cookie[] cookies = nativeRequest.getCookies();
			for (Cookie cookie : cookies) {
	            if(cookie.getName().equals(applicationKey + "_user")) {
	            	return cookie.getValue();
	            }
            }

			return null;
		} else {
			return WebArgumentResolver.UNRESOLVED;
		}
	}
}
