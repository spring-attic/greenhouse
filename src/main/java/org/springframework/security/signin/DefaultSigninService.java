package org.springframework.security.signin;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

public class DefaultSigninService implements SigninService {

	private AuthenticationManager authenticationManager;
	
	public DefaultSigninService(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void signin(String username, String password, Object requestDetails) {
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
			token.setDetails(requestDetails);
			Authentication authentication = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (AuthenticationException e) {
			SecurityContextHolder.getContext().setAuthentication(null);
		}
	}

}
