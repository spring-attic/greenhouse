/*
 * Copyright 2012 the original author or authors.
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