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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.springsource.greenhouse.utils.Location;

/**
 * Resolves the user's geo location from client-submitted query parameters, if present in the request.
 * @author Keith Donald
 */
public class UserLocationHandlerInterceptor implements HandlerInterceptor {

	public static final String USER_LOCATION_ATTRIBUTE = "userLocation";

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Double latitude = ServletRequestUtils.getDoubleParameter(request, "latitude");
		Double longitude = ServletRequestUtils.getDoubleParameter(request, "longitude");
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
