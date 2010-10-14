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
	
}