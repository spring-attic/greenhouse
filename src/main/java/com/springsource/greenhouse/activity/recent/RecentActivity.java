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
package com.springsource.greenhouse.activity.recent;

/**
 * Models a recent activity that occurred in the application.
 * Represents an item in a recent activity feed.
 * TODO: the structure of this object may be too HTML presentation-centric.
 * @author Keith Donald
 */
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