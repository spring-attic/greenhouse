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

import java.util.List;

import org.joda.time.LocalDate;

/**
 * Data access interface for {@link Event Events}
 * @author Keith Donald
 */
public interface EventRepository {

	/**
	 * Find all events that are coming up soon relative to the client's time.
	 * How "soon" is defined is left up to this implementation.
	 * For example, it may mean "in the next year" or it may simply mean "in the future".
	 * @param clientTime the time on the client device; may be intentionally set to a date in the past or future
	 * @return the list of Events coming up, sorted ascending by their start time.
	 */
	List<Event> findUpcomingEvents(Long clientTime);

	/**
	 * Get the details of an event.
	 * Used to show Event details in a web browser at a friendly URL such as /events/2010/10/chicago.
	 * @param group the group that organized the Event
	 * @param year the year the Event happens in (based on Event timezone)
	 * @param month the month the Event happens in (based on Event timezone)
	 * @param slug the short, meaningful label for this Event
	 */
	Event findEventBySlug(String group, Integer year, Integer month, String slug);

	/**
	 * Get the string to search for to find conversations, such as tweets, about the event.
	 * @param eventId the internal event identifier
	 */
	String findEventSearchString(Long eventId);

	/**
	 * Get the string to search for to find conversations such as tweets, covering an event session.
	 * @param eventId the internal event identifier
	 */
	String findSessionSearchString(Long eventId, Integer sessionId);

	/**
	 * Get the list of sessions that occur on a day for an attendee.
	 * The LocalDate specifying the day is converted to a Interval that begins at the start of the day (inclusive) and ends at the start of the next day (exclusive).
	 * The Event's {@link Event#getTimeZone() timezone} is applied in this conversion to arrive at the correct milliseconds range.
	 * @param eventId the internal Event identifier
	 * @param day the day an attendee
	 * @param attendeeId the id of the member making the request; used to calculate Session favorite information
	 * @return the list of EventSessions on the day specified, sorted ascending by start time and track
	 */
	List<EventSession> findSessionsOnDay(Long eventId, LocalDate day, Long attendeeId);

	/**
	 * Get the favorite sessions at this Event.
	 * Attendees had previously marked these sessions as their favorites, typically after reviewing the session schedule by day.
	 * @param eventId the internal Event identifier
	 * @param attendeeId the id of the attendee's member account
	 * @return the list of favorites, sorted by the most frequently favorited
	 */
	List<EventSession> findEventFavorites(Long eventId, Long attendeeId);

	/**
	 * Get the attendee's favorite sessions.
	 * The attendee had previously marked these sessions as his or her favorites, typically after reviewing the session schedule by day.
	 * @param eventId the internal Event identifier
	 * @param attendeeId the id of the attendee's member account
	 * @return the list of attendee favorites
	 */
	List<EventSession> findAttendeeFavorites(Long eventId, Long attendeeId);

	/**
	 * Toggle the attendee's favorite status for a session.
	 * @param eventId the internal event id
	 * @param sessionId the internal session id, unique relative to the event
	 * @param attendeeId the id of the attendee's member account
	 * @return true if the session is now a favorite, false if the session is no longer a favorite
	 */
	boolean toggleFavorite(Long eventId, Integer sessionId, Long attendeeId);

	/**
	 * Rate a session on behalf of an attendee.
	 * @param eventId the internal id of the event
	 * @param sessionId the internal id of the session, unique relative to the event
	 * @param attendeeId the attendee performing the rating, a reference to the attendee's member account
	 * @param rating the attendee's rating value
	 * @return the new average rating for the session, calculated again after applying the attendee's rating
	 * @throws RatingPeriodClosedException the rating period for the session is not open
	 */
	Float rate(Long eventId, Integer sessionId, Long attendeeId, Rating rating) throws RatingPeriodClosedException;
		
}