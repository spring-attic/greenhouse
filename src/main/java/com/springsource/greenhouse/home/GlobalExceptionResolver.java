package com.springsource.greenhouse.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.core.AccountNotConnectedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	private final DefaultHandlerExceptionResolver defaultExceptionHandler = new DefaultHandlerExceptionResolver();
		
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		if (ex instanceof AccountNotConnectedException) {
			try {
				logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", ex);				
				return handleAccountNotConnectedException((AccountNotConnectedException) ex, request, response, handler);
			} catch (Exception e) {
				return null;
			}
		} else {
			return defaultExceptionHandler.resolveException(request, response, handler, ex);
		}
	}

	private ModelAndView handleAccountNotConnectedException(AccountNotConnectedException ex,
			HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
		return new ModelAndView();
	}

}