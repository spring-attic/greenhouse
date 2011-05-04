/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.develop;

/**
 * A short summary for the application, suitable for display as a listing.
 * @author Keith Donald
 */
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

	/**
	 * The name of the app.
	 */
	public String getName() {
		return name;
	}

	/**
	 * A link to the application's icon, suitable for display as a thumbnail.
	 */
	public String getIconUrl() {
		return iconUrl;
	}

	/**
	 * Short description of the app.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Short, unique, and meaningful key for the application.
	 * Used in browser URLs that reference the app resource.
	 */
	public String getSlug() {
		return slug;
	}
	
}