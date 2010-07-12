package com.springsource.greenhouse.events;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/events")
public class EventsController {
	
	private GreenhouseEventsService eventsService;
	private TwitterOperations twitter;
	
	@Inject
	public EventsController(GreenhouseEventsService eventsService, TwitterOperations twitter) {
		this.eventsService = eventsService;
		this.twitter = twitter;
	}
	
	@RequestMapping(method=RequestMethod.GET, headers="Accept=application/json") 
	public @ResponseBody List<Event> eventsData() {
		return eventsService.getEvents();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String listEvents(Model model) {
		model.addAttribute(eventsService.getEvents());
		return "events/list";
	}

	@RequestMapping(value="/{eventId}", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody Event eventData(@PathVariable long eventId) {
		return eventsService.getEventById(eventId);
	}

	@RequestMapping(value="/{eventId}", method=RequestMethod.GET)
	public String viewEvent(OAuthConsumerToken accessToken, @PathVariable long eventId, Model model) {
		Event event = eventsService.getEventById(eventId);
		model.addAttribute(event);
		model.addAttribute(twitter.search(accessToken, event.getHashtag()));
		return "events/view";
	}
}
