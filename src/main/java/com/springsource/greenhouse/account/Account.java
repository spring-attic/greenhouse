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

import java.io.Serializable;

import org.springframework.web.util.UriTemplate;

/**
 * Models the identity of a local member, or user, of the application.
 * Designed to serve as the user Principal upon authentication.
 * Exposes the state required to render a link to the member's profile, as well as send e-mail correspondance.
 * @author Keith Donald
 */
@SuppressWarnings("serial")
public class Account implements Serializable {
	
	private final Long id;
	
	private final String firstName;
	
	private final String lastName;
	
	private final String email;
	
	private final String username;
	
	private final String pictureUrl;
	
	private final String profileUrl;
	
	/**
	 * Constructs an Account object.
	 * The profielUrl parameter is a UriTemplate to allow the URL to be re-generated if the Account is updated.
	 */
	public Account(Long id, String firstName, String lastName, String email, String username, String pictureUrl, UriTemplate profileUrlTemplate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.pictureUrl = pictureUrl;
		this.profileUrl = profileUrlTemplate.expand(getProfileId()).toString();
	}
	
	/**
	 * The internal identifier of this account.  Unique across all accounts.
	 * When possible, kept internal and not shared with members.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * The first name of the person associated with this account.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * The last name, or surname, of the person associated with this account.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Convenience operation that derives the full name of the account holder by combining his or her first and last names.
	 */
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	/**
	 * The email address on file for this account.
	 * May be unique across all accounts.  If so, may be used as a credential in user authentication scenarios.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * The username on file for this account.  Optional.  When present, is unique across all accounts.
	 * If present, preferred as a credential in user authentication scenarios.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * The absolute URL to a picture of the member holding this account.
	 * The linked picture should be designed to be used as an "avatar" and suitable for display as a thumb-nail.
	 */
	public String getPictureUrl() {
		return pictureUrl;
	}

	/**
	 * The absolute URL to the user's profile page.
	 * A visitor can traverse this link to view the public profile of the member associated with this account.
	 */
	public String getProfileUrl() {
		return profileUrl;
	}

	/**
	 * The key used to lookup the member's public profile.
	 * Set to the account's {@link #getUsername() username}, unless the username is null.
	 * If the username is null, set to the account's {@link #getId() internal id}.
	 */
	public String getProfileId() {
		return username != null ? username : id.toString(); 
	}
	
	// for SpringSecurity to fetch Principal#getName ... TODO favor implementing Principal directly in SPR 3.1.0.RC2 and >
	
	public String toString() {
		return id.toString();
	}
}