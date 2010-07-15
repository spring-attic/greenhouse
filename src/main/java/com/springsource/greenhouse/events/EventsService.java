package com.springsource.greenhouse.events;

import java.util.Date;
import java.util.List;

public interface EventsService {

	List<Event> getEventsAfter(Date afterDate);

	Event getEventById(long eventId);

	List<EventSession> getSessionsByEventId(long eventId);

	Event findEventByGroupNameAndEventName(String groupName, String eventName);

}