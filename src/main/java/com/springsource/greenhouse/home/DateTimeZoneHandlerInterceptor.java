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
		JodaTimeContextHolder.setJodaTimeContext(null);
	}

	private DateTimeZone getTimeZone(HttpServletRequest request) {
		Integer hoursOffset = getHoursOffset(request);
		if (hoursOffset != null) {
			try {
				return DateTimeZone.forOffsetHours(hoursOffset);
			} catch (IllegalArgumentException e) {
				return DateTimeZone.getDefault();
			}
		} else {
			return DateTimeZone.getDefault();
		}
	}
	
	private Integer getHoursOffset(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, "Greenhouse.timeZoneOffset");
		if (cookie != null ) {
			return Integer.valueOf(cookie.getValue());
		} else {
			return null;
		}
	}

}
