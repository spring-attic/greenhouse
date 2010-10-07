package com.springsource.greenhouse.events;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.twitter.SearchResults;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.Location;

@Controller
@RequestMapping("/events")
public class EventsController {
	
	private EventRepository eventRepository;
	
	private TwitterOperations twitterApi;
		
	@Inject
	public EventsController(EventRepository eventRepository, TwitterOperations twitterApi) {
		this.eventRepository = eventRepository;
		this.twitterApi = twitterApi;
	}
	
	// for web service (JSON) clients
	
	@RequestMapping(method=RequestMethod.GET, headers="Accept=application/json") 
	public @ResponseBody List<Event> upcomingEvents(@RequestParam(value="after", required=false) @DateTimeFormat(iso=ISO.DATE_TIME) Long afterMillis) {
		return eventRepository.findUpcomingEvents(afterMillis);
	}

	@RequestMapping(value="/{eventId}/favorites", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> favorites(@PathVariable Long eventId, Account account) {
		return eventRepository.findEventFavorites(eventId, account.getId());
	}
	
	@RequestMapping(value="/{eventId}/tweets", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody SearchResults tweets(@PathVariable Long eventId,  @RequestParam(defaultValue="1") Integer page, @RequestParam(defaultValue="10") Integer pageSize) {
		String searchString = eventRepository.findEventSearchString(eventId);
		return searchString != null && searchString.length() > 0 ? twitterApi.search(searchString, page, pageSize) : null;
	}

	@RequestMapping(value = "/{eventId}/tweets", method = RequestMethod.POST)
	public ResponseEntity<String> postTweet(@PathVariable Long eventId, @RequestParam String status, Location currentLocation) {
		twitterApi.tweet(status);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/{eventId}/retweet", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> postRetweet(@PathVariable Long eventId, @RequestParam Long tweetId) {
		twitterApi.retweet(tweetId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value="/{eventId}/sessions/favorites", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> favoriteSessions(@PathVariable Long eventId, Account account) {
		return eventRepository.findAttendeeFavorites(eventId, account.getId());
	}

	@RequestMapping(value="/{eventId}/sessions/{day}", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> sessionsOnDay(@PathVariable Long eventId, @PathVariable @DateTimeFormat(iso=ISO.DATE) LocalDate day, Account account) {
		return eventRepository.findSessionsOnDay(eventId, day, account.getId());
	}

	@RequestMapping(value="/{eventId}/sessions/{sessionId}/favorite", method=RequestMethod.PUT)
	public @ResponseBody Boolean toggleFavorite(@PathVariable Long eventId, @PathVariable Integer sessionId, Account account) {
		return eventRepository.toggleFavorite(eventId, sessionId, account.getId());
	}

	@RequestMapping(value="/{eventId}/sessions/{sessionId}/rating", method=RequestMethod.POST)
	public @ResponseBody Float updateRating(@PathVariable Long eventId, @PathVariable Integer sessionId, Account account, @RequestParam Short value, @RequestParam String comment) throws SessionNotEndedException {
		return eventRepository.rate(eventId, sessionId, account.getId(), value, comment);
	}

	@RequestMapping(value="/{eventId}/sessions/{sessionId}/tweets", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody SearchResults sessionTweets(@PathVariable Long eventId, @PathVariable Integer sessionId, @RequestParam(defaultValue="1") Integer page, @RequestParam(defaultValue="10") Integer pageSize) {
		String searchString = eventRepository.findSessionSearchString(eventId, sessionId);
		return searchString != null && searchString.length() > 0 ? twitterApi.search(searchString, page, pageSize) : null;
	}

	@RequestMapping(value="/{eventId}/sessions/{sessionId}/tweets", method=RequestMethod.POST)
	public ResponseEntity<String> postSessionTweet(@PathVariable Long eventId, @PathVariable Integer sessionId, @RequestParam String status, Location currentLocation) {
		twitterApi.tweet(status);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/{eventId}/sessions/{sessionId}/retweet", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> postSessionRetweet(@PathVariable Long eventId, @PathVariable Integer sessionId, @RequestParam Long tweetId) {
		twitterApi.retweet(tweetId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	// for web browser (HTML) clients
	
	@RequestMapping(method=RequestMethod.GET, headers="Accept=text/html")
	public String upcomingEventsView(Model model, DateTimeZone timeZone) {
		model.addAttribute(eventRepository.findUpcomingEvents(new DateTime(timeZone).getMillis()));
		return "events/list";
	}
	
}