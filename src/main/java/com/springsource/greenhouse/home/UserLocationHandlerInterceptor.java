package com.springsource.greenhouse.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.springsource.greenhouse.action.Location;

public class UserLocationHandlerInterceptor implements HandlerInterceptor {

	public static final String USER_LOCATION_ATTRIBUTE = "userLocation";

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Float latitude = ServletRequestUtils.getFloatParameter(request, "latitude");
		Float longitude = ServletRequestUtils.getFloatParameter(request, "longitude");
		if (latitude != null && longitude != null) {
			request.setAttribute(USER_LOCATION_ATTRIBUTE, new Location(latitude, longitude));
		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}

}
