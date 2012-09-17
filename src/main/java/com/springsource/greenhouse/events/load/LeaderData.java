/*
 * Copyright 2012 the original author or authors.
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
package com.springsource.greenhouse.events.load;

public class LeaderData {

	private final String name;
	private final String bio;
	private final String personalUrl;
	private final String twitterId;
	private final String source;
	private final long sourceId;

	public LeaderData(String name, String bio, String personalUrl, String twitterId, String source, long sourceId) {
		this.name = name;
		this.bio = bio;
		this.personalUrl = personalUrl;
		this.twitterId = twitterId;
		this.source = source;
		this.sourceId = sourceId;
	}

	public String getName() {
		return name;
	}

	public String getBio() {
		return bio;
	}

	public String getPersonalUrl() {
		return personalUrl;
	}

	public String getTwitterId() {
		return twitterId;
	}

	public String getSource() {
		return source;
	}

	public long getSourceId() {
		return sourceId;
	}

}
