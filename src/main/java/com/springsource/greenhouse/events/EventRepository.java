package com.springsource.greenhouse.events;

import java.util.List;

public interface EventRepository {

	List<Event> findUpcomingEvents();

	List<Event> findEventsByMonthOfYear(String group, Integer month, Integer year);

	Event findEventByName(String group, Integer month, Integer year, String name);

	String findEventHashtag(Long eventId);

	String findSessionHashtag(Long eventId, Short sessionCode);

	List<EventSession> findTodaysSessions(Long eventId);
	
}