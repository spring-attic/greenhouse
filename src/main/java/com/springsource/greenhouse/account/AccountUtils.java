package com.springsource.greenhouse.account;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


public final class AccountUtils {

	public static void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(authenticationTokenFor(account));
	}
	
	public static Authentication authenticationTokenFor(Account account) {
		return new UsernamePasswordAuthenticationToken(account, null, (Collection<GrantedAuthority>)null);		
	}
	
}
