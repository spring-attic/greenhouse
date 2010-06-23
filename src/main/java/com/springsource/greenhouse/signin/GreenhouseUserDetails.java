package com.springsource.greenhouse.signin;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public final class GreenhouseUserDetails implements UserDetails {
	
	private final Long entityId;
	
	private final String username;
	
	private final String password;
	
	private final String firstName;
	
	public GreenhouseUserDetails(Long entityId, String username, String password, String firstName) {
		this.entityId = entityId;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
	}
	
	public Long getEntityId() {
		return entityId;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getProfileKey() {
		if (username != null && username.length() == 0) {
			return username;
		} else {
			return entityId.toString();
		}
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