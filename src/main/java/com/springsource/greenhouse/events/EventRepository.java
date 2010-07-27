package com.springsource.greenhouse.events;

import java.util.List;

public interface EventRepository {

	List<Event> findUpcomingEvents();

	String findEventHashtag(Long eventId);

	String findSessionHashtag(Long eventId, Short sessionCode);

	List<EventSession> findTodaysSessions(Long eventId);
	
}