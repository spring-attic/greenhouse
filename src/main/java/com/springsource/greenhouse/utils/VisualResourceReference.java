package com.springsource.greenhouse.utils;

public class VisualResourceReference<T> extends ResourceReference<T> {
	
	private final String imageUrl;
	
	public VisualResourceReference(T id, String label, String imageUrl) {
		super(id, label);
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}
	
}
