package com.springsource.greenhouse.signin;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountNotFoundException;
import com.springsource.greenhouse.account.InvalidPasswordException;
import com.springsource.greenhouse.account.UsernamePasswordAuthenticationService;

public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

	private UsernamePasswordAuthenticationService authenticationService;
	
	public UsernamePasswordAuthenticationProvider(UsernamePasswordAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		try {
			Account account = authenticationService.authenticate(token.getName(), (String) token.getCredentials());
			return authenticatedToken(account, authentication);
		} catch (AccountNotFoundException e) {
			throw new UsernameNotFoundException(token.getName(), e);
		} catch (InvalidPasswordException e) {
			throw new BadCredentialsException("Invalid password", e);
		}
	}

	public boolean supports(Class<? extends Object> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}
	
	// internal helpers
	
	private Authentication authenticatedToken(Account account, Authentication original) {
		List<GrantedAuthority> authorities = null;
		UsernamePasswordAuthenticationToken authenticated = new UsernamePasswordAuthenticationToken(account, null, authorities);
		authenticated.setDetails(original.getDetails());
		return authenticated;		
	}

}