/*
 * Copyright 2010 the original author or authors.
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
package com.springsource.greenhouse.home;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTimeZone;
import org.springframework.format.datetime.joda.JodaTimeContext;
import org.springframework.format.datetime.joda.JodaTimeContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

/**
 * Spring MVC Interceptor that sets the request's {@link DateTimeZone} from a cookie.
 * Allows the client's timezone to be figured out by JavaScript, then submitted and applied on the server as the default timezone for the request.
 * Useful when you need to render dates in client local time.
 * @author Keith Donald
 */
public class DateTimeZoneHandlerInterceptor implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		JodaTimeContext context = new JodaTimeContext();
		context.setTimeZone(getTimeZone(request));
		JodaTimeContextHolder.setJodaTimeContext(context);
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			modelAndView.addObject("jodaTimeContext", JodaTimeContextHolder.getJodaTimeContext());
		}
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		JodaTimeContextHolder.resetJodaTimeContext();
	}

	// interna helpers
	
	private DateTimeZone getTimeZone(HttpServletRequest request) {
		Integer millisOffset = getMillisOffset(request);
		if (millisOffset != null) {
			try {
				return DateTimeZone.forOffsetMillis(millisOffset);
			} catch (IllegalArgumentException e) {
				return DateTimeZone.getDefault();
			}
		} else {
			return DateTimeZone.getDefault();
		}
	}
	
	private Integer getMillisOffset(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, "Greenhouse.timeZoneOffset");
		if (cookie != null ) {
			return Integer.valueOf(cookie.getValue());
		} else {
			return null;
		}
	}

}
