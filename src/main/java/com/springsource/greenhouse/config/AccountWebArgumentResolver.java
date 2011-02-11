package com.springsource.greenhouse.config;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import com.springsource.greenhouse.account.Account;

/**
 * Resolves {@link Account} @Controller method parameter values from the current request's Authentication userPrincipal.
 * @author Keith Donald
 */
public class AccountWebArgumentResolver implements WebArgumentResolver {
	public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
		if (Account.class.isAssignableFrom(param.getParameterType())) {
			Authentication auth = (Authentication) request.getUserPrincipal();
			return auth != null && auth.getPrincipal() instanceof Account ? auth.getPrincipal() : null;
		} else {
			return WebArgumentResolver.UNRESOLVED;
		}
	}
}