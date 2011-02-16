package com.springsource.greenhouse.signin;

import java.io.Serializable;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.web.connect.SignInControllerGateway;

public class AccountAsPrincipalSigninGateway implements SignInControllerGateway {

	public void signIn(Serializable account) {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(account, null));
	}

}
