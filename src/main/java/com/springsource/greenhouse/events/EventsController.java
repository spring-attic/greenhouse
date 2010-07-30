package com.springsource.greenhouse.events;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.oauth.extras.OAuthAccessToken;
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
	
	private EventRepository eventRepository;
	
	private TwitterOperations twitter;
		
	@Inject
	public EventsController(EventRepository eventRepository, TwitterOperations twitter) {
		this.eventRepository = eventRepository;
		this.twitter = twitter;
	}
	
	// for web service (JSON) clients
	
	@RequestMapping(method=RequestMethod.GET, headers="Accept=application/json") 
	public @ResponseBody List<Event> upcomingEvents() {
		return eventRepository.findUpcomingEvents();
	}

	@RequestMapping(value="/{id}/tweets", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody SearchResults tweets(@PathVariable Long id,  @RequestParam(defaultValue="1") Integer page, @RequestParam(defaultValue="10") Integer pageSize) {
		return twitter.search(eventRepository.findEventHashtag(id), page, pageSize);
	}

	@RequestMapping(value="/{id}/tweets", method=RequestMethod.POST)
	public @ResponseBody void postTweet(@PathVariable Long eventId, @RequestParam String status, @OAuthAccessToken("twitter") OAuthConsumerToken accessToken) {
		twitter.updateStatus(accessToken, status);
	}

	@RequestMapping(value="/{id}/sessions/today", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> sessionsToday(@PathVariable Long id) {
		return eventRepository.findTodaysSessions(id);
	}
	
	@RequestMapping(value="/{id}/sessions/{date}", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> sessionsToday(@PathVariable Long id, @PathVariable @DateTimeFormat(iso=ISO.DATE) Date date) {
		return eventRepository.findSessionsByDate(id, date);
	}

	@RequestMapping(value="/{id}/sessions/{number}/tweets", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody SearchResults sessionTweets(@PathVariable Long id, @PathVariable Short number, @RequestParam(defaultValue="1") Integer page, @RequestParam(defaultValue="10") Integer pageSize) {
		return twitter.search(eventRepository.findSessionHashtag(id, number), page, pageSize);
	}

	@RequestMapping(value="/{id}/sessions/{number}/tweets", method=RequestMethod.POST)
	public @ResponseBody void postSessionTweet(@PathVariable Long id, @PathVariable Short number, @RequestParam String status, @OAuthAccessToken("twitter") OAuthConsumerToken accessToken) {
		twitter.updateStatus(accessToken, status);
	}

	// for web browser (HTML) clients
	
	@RequestMapping(method=RequestMethod.GET, headers="Accept=text/html")
	public String upcomingEventsView(Model model) {
		model.addAttribute(eventRepository.findUpcomingEvents());
		return "events/list";
	}
	
}