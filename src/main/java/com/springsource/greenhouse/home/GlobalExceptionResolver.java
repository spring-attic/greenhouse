package com.springsource.greenhouse.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.social.core.AccountNotConnectedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	private final ResponseStatusExceptionResolver annotatedExceptionHandler = new ResponseStatusExceptionResolver();
	
	private final DefaultHandlerExceptionResolver defaultExceptionHandler = new DefaultHandlerExceptionResolver();
		
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		try {
			if (ex instanceof AccountNotConnectedException) {
				return handleAccountNotConnectedException((AccountNotConnectedException) ex, request, response, handler);
			} else if (ex instanceof EmptyResultDataAccessException) {
				return handleEmptyResultDataAccessException((EmptyResultDataAccessException) ex, request, response, handler);
			}
		} catch (Exception e) {
			logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", ex);
			return null;
		}
		ModelAndView result = annotatedExceptionHandler.resolveException(request, response, handler, ex);
		if (result != null) {
			return result;
		}
		return defaultExceptionHandler.resolveException(request, response, handler, ex);
	}

	private ModelAndView handleAccountNotConnectedException(AccountNotConnectedException ex,
			HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
		return new ModelAndView();
	}

	private ModelAndView handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
			HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
		return new ModelAndView();
	}

}