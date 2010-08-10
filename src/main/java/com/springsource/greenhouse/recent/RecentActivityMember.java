package com.springsource.greenhouse.recent;

public class RecentActivityMember {
	
	private final String displayName;
	
	private final String location;
	
	private final String profileUrl;
	
	private final String profilePicUrl;

	public RecentActivityMember(String displayName, String location, String profileUrl, String profilePicUrl) {
		this.displayName = displayName;
		this.location = location;
		this.profileUrl = profileUrl;
		this.profilePicUrl = profilePicUrl;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getLocation() {
		return location;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public String getProfilePicUrl() {
		return profilePicUrl;
	}
		
}