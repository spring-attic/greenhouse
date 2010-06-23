package com.springsource.greenhouse.signin;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public final class GreenhouseUserDetails implements UserDetails {
	
	private final String username;
	
	private final String password;
	
	private final String id;
	
	private final String firstName;
	
	public GreenhouseUserDetails(String username, String password, String id, String firstName) {
		this.username = username;
		this.password = password;
		this.id = id;
		this.firstName = firstName;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		return Collections.emptySet();
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}		
}