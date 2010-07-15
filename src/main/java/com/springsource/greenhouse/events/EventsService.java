package com.springsource.greenhouse.events;

import java.util.Date;
import java.util.List;

public interface EventsService {

	List<Event> findEventsAfter(Date afterDate);

	Event findEventById(long eventId);

	List<EventSession> findSessionsByEventId(long eventId);
		
	Event findEventByPublicId(String eventName);
}