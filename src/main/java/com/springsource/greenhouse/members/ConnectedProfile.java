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
 * A reference to a profile a member has on another network, such as Facebook or Twitter.
 * @author Keith Donald
 */
public final class ConnectedProfile {
	
	private final String name;
	
	private final String url;

	public ConnectedProfile(String name, String url) {
		this.name = name;
		this.url = url;
	}

	/**
	 * The display name of the service provider or network the member has connected to.
	 */
	public String getName() {
		return name;
	}

	/**
	 * The URL of the member's profile on the provider's network.
	 */
	public String getUrl() {
		return url;
	}
	
}
