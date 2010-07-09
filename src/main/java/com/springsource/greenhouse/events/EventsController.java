package com.springsource.greenhouse.events;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/events")
public class EventsController {
	
	private GreenhouseEventsService eventsService;
	
	@Inject
	public EventsController(GreenhouseEventsService eventsService) {
		this.eventsService = eventsService;
	}
	
	@RequestMapping(method=RequestMethod.GET)  //, headers="Accept=application/json") 
	public @ResponseBody List<Event> eventsData() {
		return eventsService.getEvents();
	}

	@RequestMapping(value="/event/{eventId}", method=RequestMethod.GET)
	public @ResponseBody Event eventData(@PathVariable long eventId) {
		return eventsService.getEventById(eventId);
	}
	
}
