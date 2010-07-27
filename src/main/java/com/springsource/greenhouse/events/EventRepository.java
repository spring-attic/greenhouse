package com.springsource.greenhouse.events;

import java.util.List;

public interface EventRepository {

	List<Event> findUpcomingEvents();

	String getEventSearchString(Long eventId);

	String getEventSessionSearchString(Long eventId, Short sessionCode);

	List<EventSession> findTodaysSessions(Long eventId);
	
}