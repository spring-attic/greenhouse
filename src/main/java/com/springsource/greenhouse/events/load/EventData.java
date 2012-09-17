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

class EventData {

	private final long memberGroupId;
	private final String name;
	private final String description;
	private final String abbreviation;
	private final String firstDay;
	private final String lastDay;
	private final String timeZone;
	private final String source;
	private final long sourceId;

	public EventData(long memberGroupId, String name, String description, String abbreviation, String firstDay, String lastDay, String timeZone, String source, long sourceId) {
		this.memberGroupId = memberGroupId;
		this.name = name;
		this.description = description;
		this.abbreviation = abbreviation;
		this.firstDay = firstDay;
		this.lastDay = lastDay;
		this.timeZone = timeZone;
		this.source = source;
		this.sourceId = sourceId;
	}
	
	public long getMemberGroupId() {
		return memberGroupId;
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public String getFirstDay() {
		return firstDay;
	}

	public String getLastDay() {
		return lastDay;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public String getSource() {
		return source;
	}

	public long getSourceId() {
		return sourceId;
	}
	
}
