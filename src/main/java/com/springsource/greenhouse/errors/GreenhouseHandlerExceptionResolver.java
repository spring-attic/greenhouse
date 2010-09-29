package com.springsource.greenhouse.errors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.social.core.AccountNotConnectedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

@Component
public class GreenhouseHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		try {
			if (ex instanceof AccountNotConnectedException) {
				return handleAccountNotConnectedException((AccountNotConnectedException) ex, request, response, handler);
			}
		} catch (Exception handlerException) {
			logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
		}

		return null;
	}

	private ModelAndView handleAccountNotConnectedException(AccountNotConnectedException ex,
			HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
		return new ModelAndView();
	}

}
