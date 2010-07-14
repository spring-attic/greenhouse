package com.springsource.greenhouse.events;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.social.twitter.SearchResults;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/events")
public class EventsController {
	
	private EventsService eventsService;
	private TwitterOperations twitter;
	
	@Inject
	public EventsController(EventsService eventsService, TwitterOperations twitter) {
		this.eventsService = eventsService;
		this.twitter = twitter;
	}
	
	@RequestMapping(method=RequestMethod.GET, headers="Accept=application/json") 
	public @ResponseBody List<Event> eventsData() {
		return eventsService.getEventsAfter(new Date());
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String listEvents(Model model) {
		model.addAttribute(eventsService.getEventsAfter(new Date()));
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
	
	@RequestMapping(value="/{eventId}/tweets", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody SearchResults listEventTweets(OAuthConsumerToken accessToken, @PathVariable long eventId, 
			@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="20") int perPage) {
		Event event = eventsService.getEventById(eventId);
		return twitter.search(accessToken, event.getHashtag(), page, perPage);
	}
}
