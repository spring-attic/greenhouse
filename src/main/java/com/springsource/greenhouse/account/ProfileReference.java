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
package com.springsource.greenhouse.account;

import com.springsource.greenhouse.utils.VisualResourceReference;

/**
 * A reference to a member profile.
 * Used to render a link to the profile.
 * The link may contain text and a img.
 * The link text is generally the member's full name.
 * @author Keith Donald
 */
public class ProfileReference extends VisualResourceReference<String> {

	public ProfileReference(String id, String label, String imageUrl) {
		super(id, label, imageUrl);
	}

	public static ProfileReference textOnly(Long id, String username, String firstName, String lastName) {
		return new ProfileReference(username != null && username.length() > 0 ? username : id.toString(), firstName + " " + lastName, null);
	}
	
}