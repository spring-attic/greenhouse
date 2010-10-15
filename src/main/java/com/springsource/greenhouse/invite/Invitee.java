package com.springsource.greenhouse.invite;

import com.springsource.greenhouse.utils.EmailUtils;

public final class Invitee {

	private final String firstName;
	
	private final String lastName;
	
	private final String email;

	public Invitee(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		if (!EmailUtils.isEmail(email)) {
			throw new IllegalArgumentException("'" + email +  "' is not a valid email address");
		}
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public static Invitee valueOf(String inviteeString) {
		if (inviteeString == null || inviteeString.length() == 0) {
			throw new IllegalArgumentException("The Invitee string to parse cannot be null or empty");
		}
		String[] pieces = inviteeString.split("[<>]");
		if (pieces.length == 1) {
			return new Invitee(pieces[0]);	
		} else if (pieces.length == 2) {
			String[] name = pieces[0].split(" ");
			String email = pieces[1];
			if (name.length == 1) {
				if (name[0].length() == 0) {
					return new Invitee(email);
				} else {
					return new Invitee(name[0], email);
				}
			} else if (name.length == 2) {
				return new Invitee(name[0], name[1], pieces[1]);
			}
		}
		throw new IllegalArgumentException("Unable to parse invalid invitee string '" + inviteeString + "'");		
	}

	public String toString() {
		if (firstName == null && lastName == null) {
			return email;
		}
		StringBuilder builder = new StringBuilder();
		if (firstName != null) {
			builder.append(firstName);
			builder.append(' ');
		}
		if (lastName != null) {
			builder.append(lastName);
			builder.append(' ');
		}
		builder.append('<').append(email).append('>');
		return builder.toString();
	}
	
	// internal helpers
	
	private Invitee(String email) {
		this(null, null, email);
	}

	private Invitee(String firstName, String email) {
		this(firstName, null, email);
	}

}
