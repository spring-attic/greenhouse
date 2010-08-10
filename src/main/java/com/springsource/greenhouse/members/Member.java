package com.springsource.greenhouse.members;

public class Member {
	
	private String firstName;
	
	private String lastName;
	
	private String profileImageUrl;

	public String getDisplayName() {
		return firstName + " " + lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setProfileImageUrl(String profileImageUrl) {
	    this.profileImageUrl = profileImageUrl;
    }

	public String getProfileImageUrl() {
	    return profileImageUrl;
    }
	
}
