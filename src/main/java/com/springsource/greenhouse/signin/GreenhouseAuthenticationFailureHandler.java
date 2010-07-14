package com.springsource.greenhouse.signin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.flash.FlashMap;

public class GreenhouseAuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		FlashMap.getCurrent(request).put("signinError", true);
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/signin"));
	}
	
}
