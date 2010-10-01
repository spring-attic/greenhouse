package com.springsource.greenhouse.events;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.springsource.greenhouse.utils.ResourceReference;

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

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return this.title;
	}

	public DateTimeZone getTimeZone() {
		return timeZone;
	}
	
	public DateTime getStartTime() {
		return startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public String getSlug() {
		return slug;
	}
	
	public String getDescription() {
		return description;
	}

	public String getHashtag() {
		return hashtag;
	}

	public ResourceReference<String> getGroup() {
		return group;
	}
	
	public Set<Venue> getVenues() {
		return Collections.unmodifiableSet(venues);
	}

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