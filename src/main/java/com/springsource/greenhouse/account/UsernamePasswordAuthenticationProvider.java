/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.account;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * Adapts {@link AccountRepository#authenticate(String, String)} to the SpringSecurity AuthenticationProvider SPI.
 * Allows the AccountRepository to drive authentication in a Spring Security environment.
 * The authenticated Account is treated as the {@link Authentication#getPrincipal() Authentication Principal}.
 * @author Keith Donald
 */
@Service
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
		} catch (SignInNotFoundException e) {
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