package com.springsource.greenhouse.connect;

import com.springsource.greenhouse.utils.ResourceReference;

public class AccountReference extends ResourceReference<String> {

	private final String pictureUrl;
	
	public AccountReference(String id, String label, String pictureUrl) {
		super(id, label);
		this.pictureUrl = pictureUrl;
	}
	
	public String getPictureUrl() {
		return pictureUrl;
	}

	public static AccountReference textOnly(Long id, String username, String firstName, String lastName) {
		return new AccountReference(username != null && username.length() > 0 ? username : id.toString(), firstName + " " + lastName, null);
	}
	
}