package com.springsource.greenhouse.account;

import java.io.Serializable;

import org.springframework.web.util.UriTemplate;

@SuppressWarnings("serial")
public class Account implements Serializable {
	
	private final Long id;
	
	private final String firstName;
	
	private final String lastName;
	
	private final String email;
	
	private final String username;
	
	private final String pictureUrl;
	
	private final UriTemplate profileUrlTemplate;
	
	public Account(Long id, String firstName, String lastName, String email, String username, String pictureUrl, UriTemplate profileUrlTemplate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.pictureUrl = pictureUrl;
		this.profileUrlTemplate = profileUrlTemplate;
	}
	
	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPictureUrl() {
		return pictureUrl;
	}

	public String getProfileUrl() {
		return profileUrlTemplate.expand(getProfileId()).toString();
	}

	public String getProfileId() {
		return username != null ? username : id.toString(); 
	}
	
	public Account makeUsername(String username) {
		return new Account(id, firstName, lastName, email, username, pictureUrl, profileUrlTemplate);
	}
	
}