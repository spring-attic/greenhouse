package com.springsource.greenhouse.signin;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public final class GreenhouseUserDetails implements UserDetails {
	
	private final Long entityId;
	
	private String username;
	
	private final String password;
	
	private final String firstName;

	public GreenhouseUserDetails(Long entityId, String username, String firstName) {
		this.entityId = entityId;
		this.username = username;
		this.password = null;
		this.firstName = firstName;
	}

	public GreenhouseUserDetails(Long entityId, String username, String password, String firstName) {
		this.entityId = entityId;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
	}

	// implementing UserDetails
	
	public String getUsername() {
		return username;
	}
	
	/*
	 * Breaking immutability here seems dirty. But attempts to solve this while maintaining
	 * immutability all open up a can of worms. Revisit later.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/* Returns password needed to do the initial user authentication 
	 * After authentication, this field is cleared (null) */
	public String getPassword() {
		return password;
	}

	public Long getEntityId() {
		return entityId;
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

	// additional details
	
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
	
}