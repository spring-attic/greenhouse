package com.springsource.greenhouse.recent;

public class RecentActivity {

	private final String memberPictureUrl;
	
	private final String text;

	private final String imageUrl;

	public RecentActivity(String memberPictureUrl, String text, String imageUrl) {
		this.memberPictureUrl = memberPictureUrl;
		this.text = text;
		this.imageUrl = imageUrl;
	}

	public String getMemberPictureUrl() {
		return memberPictureUrl;
	}

	public String getText() {
		return text;
	}	

	public String getImageUrl() {
		return imageUrl;
	}
	
	public String toString() {
		return "Recent Activity: " + text;
	}
	
}