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
package com.springsource.greenhouse.events;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.springsource.greenhouse.utils.ResourceReference;

/**
 * A happening organized by a group of members.
 * An event has a start time and an end time.
 * It hosts one or more sessions lead by leaders.
 * It is hosted at one or more venues.
 * @author Keith Donald
 */
public class Event {

	private final Long id;

	private final String title;

	private final DateTimeZone timeZone;
	
	private final DateTime startTime;

	private final DateTime endTime;

	private final String slug;
	
	private final String description;

	private final String hashtag;

	private final ResourceReference<String> group;
	
	private Set<Venue> venues;
	
	public Event(Long id, String title, DateTimeZone timeZone, DateTime startTime, DateTime endTime, String slug, String description, String hashtag, ResourceReference<String> group) {
		this.id = id;
		this.title = title;
		this.timeZone = timeZone;
		this.startTime = startTime;
		this.endTime = endTime;
		this.slug = slug;
		this.description = description;
		this.hashtag = hashtag;
		this.group = group;
	}

	/**
	 * The internal identifier of the Event.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * The name of the Event, concisely communicating it brand and/or purpose.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * The timezone the Event is held in.
	 * TODO: look into how DateTimeZone is serialized to JSON.
	 */
	public DateTimeZone getTimeZone() {
		return timeZone;
	}

	/**
	 * The time the Event starts.
	 * Stored in UTC time; may be converted to Event time by applying {@link #getTimeZone()}.
	 */
	public DateTime getStartTime() {
		return startTime;
	}

	/**
	 * The time the Event ends.
	 * Stored in UTC time; may be converted to Event time by applying {@link #getTimeZone()}.
	 */
	public DateTime getEndTime() {
		return endTime;
	}

	/**
	 * A Short and meaningful key for the Event, unique relative to its date/time.
	 * For example, for the Event "SpringOne2gx 2010" the slug was "chicago".
	 * Supports friendly URLs of the pattern /events/{year}/{month}/{slug} such as /events/2010/10/chicago.
	 */
	public String getSlug() {
		return slug;
	}
	
	/**
	 * A paragraph description of the Event and how it will benefit attendees. 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Search key used to identify the Twitter conversation thread covering this Event.
	 */
	public String getHashtag() {
		return hashtag;
	}

	/**
	 * A reference to the member Group that organized this Event.
	 * Used to group Events by their Group; can also support a link to the Group details page.
	 */
	public ResourceReference<String> getGroup() {
		return group;
	}
	
	/**
	 * The Venues at which this Event is held.
	 * Most Events are held at a single Venue, such as a hotel, office building, or convention center.
	 * Large Events may be held across multiple Venues e.g. multiple hotels.
	 */
	public Set<Venue> getVenues() {
		return Collections.unmodifiableSet(venues);
	}

	/**
	 * Adder used during Event instance construction time to populate the Venues.
	 * Should not be called after the Event is constructed.
	 */
	public void addVenue(Venue venue) {
		if (venues == null) {
			venues = new LinkedHashSet<Venue>();
		}
		venues.add(venue);
	}
	
	// iphone 1.0.0 compatibility
	
	// TODO here for compatibility reasons; remove when iphone app is upgraded
	public String getLocation() {
		return venues.iterator().next().getName();
	}

	public String getGroupName() {
		return group.getLabel();
	}

	public String getGroupSlug() {
		return group.getId();
	}

}