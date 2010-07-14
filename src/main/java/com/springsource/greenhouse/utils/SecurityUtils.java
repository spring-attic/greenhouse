package com.springsource.greenhouse.utils;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.springsource.greenhouse.account.Account;

public final class SecurityUtils {

	public static void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(account, null, (Collection<GrantedAuthority>)null));
	}
	
}
