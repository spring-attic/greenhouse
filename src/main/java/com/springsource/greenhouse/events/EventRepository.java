package com.springsource.greenhouse.events;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public interface EventRepository {

	List<Event> findUpcomingEvents(DateTime clientTime);

	Event findEventBySlug(String group, Integer year, Integer month, String slug);

	String findEventSearchString(Long eventId);

	String findSessionSearchString(Long eventId, Integer sessionId);

	List<EventSession> findSessionsOnDay(Long eventId, LocalDate day, Long attendeeId);

	List<EventSession> findEventFavorites(Long eventId, Long attendeeId);
	
	List<EventSession> findAttendeeFavorites(Long eventId, Long attendeeId);

	boolean toggleFavorite(Long eventId, Integer sessionId, Long attendeeId);

	void rate(Long eventId, Integer sessionId, Long attendeeId, Short value, String comment);
	
}