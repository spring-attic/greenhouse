package com.springsource.greenhouse.members;

public class Profile {
	
	private Long accountId;
	
	private String displayName;

	private String pictureUrl;
	
	public Profile(Long accountId, String displayName, String pictureUrl) {
		this.accountId = accountId;
		this.displayName = displayName;
		this.pictureUrl = pictureUrl;
	}

	public Long getAccountId() {
		return accountId;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getPictureUrl() {
		return pictureUrl;
	}
}