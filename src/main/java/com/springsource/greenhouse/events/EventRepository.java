package com.springsource.greenhouse.events;

import java.util.Date;
import java.util.List;

public interface EventRepository {

	List<Event> findUpcomingEvents();

	Event findEventByName(String group, Integer year, Integer month, String name);

	String findEventHashtag(Long eventId);

	String findSessionHashtag(Long eventId, Short sessionNumber);

	List<EventSession> findTodaysSessions(Long eventId);
	
	List<EventSession> findSessionsByDate(Long eventId, Date day);
	
}