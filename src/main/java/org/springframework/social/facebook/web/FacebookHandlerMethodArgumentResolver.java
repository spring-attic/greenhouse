package org.springframework.social.facebook.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class FacebookHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	private final String appId;
	
	private final String appSecret;
	
	public FacebookHandlerMethodArgumentResolver(String appId, String appSecret) {
		this.appId = appId;
		this.appSecret = appSecret;
	}

	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(FacebookCookieValue.class) != null;
	}

	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest request,
			WebDataBinderFactory binderFactory) throws Exception {
		FacebookCookieValue annotation = parameter.getParameterAnnotation(FacebookCookieValue.class);		
		HttpServletRequest nativeRequest = (HttpServletRequest) request.getNativeRequest();
		Map<String, String> cookieData = FacebookCookieParser.getFacebookCookieData(nativeRequest.getCookies(), appId, appSecret);
		String key = annotation.value();
		if (!cookieData.containsKey(key) && annotation.required()) {
			throw new IllegalStateException("Missing required Facebook cookie value '" + key + "'");
		}
		return cookieData.get(key);
	}

}