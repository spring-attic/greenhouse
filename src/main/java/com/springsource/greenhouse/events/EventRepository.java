package com.springsource.greenhouse.events;

import java.util.List;

import org.joda.time.LocalDate;

public interface EventRepository {

	List<Event> findUpcomingEvents();

	Event findEventByName(String group, Integer year, Integer month, String name);

	String findEventHashtag(Long eventId);

	String findSessionHashtag(Long eventId, Short sessionNumber);

	List<EventSession> findTodaysSessions(Long eventId, Long attendeeId);
	
	List<EventSession> findSessionsOnDay(Long eventId, LocalDate day, Long attendeeId);

	List<EventSession> findFavorites(Long eventId, Long attendeeId);
	
	List<EventSession> findAttendeeFavorites(Long eventId, Long attendeeId);

	boolean toggleFavorite(Long eventId, Short sessionNumber, Long attendeeId);

	void updateRating(Long eventId, Short sessionNumber, Long attendeeId, Short value, String comment);
	
}