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
package com.springsource.greenhouse.members;

/**
 * The public record containing information about a Member of our community.
 * @author Keith Donald
 */
public class Profile {
	
	private Long accountId;
	
	private String displayName;

	private String pictureUrl;
	
	public Profile(Long accountId, String displayName, String pictureUrl) {
		this.accountId = accountId;
		this.displayName = displayName;
		this.pictureUrl = pictureUrl;
	}

	/**
	 * The member's account identifier.
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * The member's display name.
	 * Typically their full name but may be just their first name or a nick name.
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * A link to the member's picture.
	 * Typically a larger version of their picture suitable for display on their own profile page.
	 */
	public String getPictureUrl() {
		return pictureUrl;
	}
}