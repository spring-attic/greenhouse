package com.springsource.greenhouse.account;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.account.Account;


public final class AccountUtils {

	public static void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(account, null, (Collection<GrantedAuthority>)null));
	}
	
}
