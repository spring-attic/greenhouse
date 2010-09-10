package com.springsource.greenhouse.develop;

public class AppSummary {
	
	private final String name;
	
	private final String iconUrl;
	
	private final String description;

	private final String slug;
	
	public AppSummary(String name, String iconUrl, String description, String slug) {
		this.name = name;
		this.iconUrl = iconUrl;
		this.description = description;
		this.slug = slug;
	}

	public String getName() {
		return name;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public String getDescription() {
		return description;
	}

	public String getSlug() {
		return slug;
	}
	
}