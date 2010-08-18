package com.springsource.greenhouse.account;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;


public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

	private AccountRepository accountRepository;
	
	@Inject
	public UsernamePasswordAuthenticationProvider(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		try {
			Account account = accountRepository.authenticate(token.getName(), (String) token.getCredentials());
			return authenticatedToken(account, authentication);
		} catch (UsernameNotFoundException e) {
			throw new org.springframework.security.core.userdetails.UsernameNotFoundException(token.getName(), e);
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