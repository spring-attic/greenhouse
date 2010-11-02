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
package org.springframework.web.flash;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter that exposes flash messages as request attributes in the next request following a redirect.
 * @author Keith Donald
 */
public class FlashMapFilter extends OncePerRequestFilter {

	@Override
	@SuppressWarnings("unchecked")
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			Map<String, ?> flash = (Map<String, ?>) session.getAttribute(FlashMap.FLASH_MAP_ATTRIBUTE);
			if (flash != null) {
				for (Map.Entry<String, ?> entry : flash.entrySet()) {
					Object currentValue = request.getAttribute(entry.getKey());
					if (currentValue == null) {
						request.setAttribute(entry.getKey(), entry.getValue());
					}					
				}
				session.removeAttribute(FlashMap.FLASH_MAP_ATTRIBUTE);
			}
		}
		filterChain.doFilter(request, response);
	}

}
