package com.springsource.greenhouse.members;

public class Profile {
	
	private Long accountId;
	
	private String displayName;
		
	public Profile(Long accountId, String displayName) {
		this.accountId = accountId;
		this.displayName = displayName;
	}

	public Long getAccountId() {
		return accountId;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}