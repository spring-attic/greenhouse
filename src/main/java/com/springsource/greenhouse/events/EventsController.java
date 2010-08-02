package com.springsource.greenhouse.events;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;
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

import com.springsource.greenhouse.account.Account;

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

	@RequestMapping(value="/{id}/favorites", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventFavorite> favorites(@PathVariable Long id) {
		return eventRepository.findFavorites(id);
	}
	
	@RequestMapping(value="/{id}/tweets", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody SearchResults tweets(@PathVariable Long id,  @RequestParam(defaultValue="1") Integer page, @RequestParam(defaultValue="10") Integer pageSize) {
		return twitter.search(eventRepository.findEventHashtag(id), page, pageSize);
	}

	@RequestMapping(value="/{id}/tweets", method=RequestMethod.POST)
	public @ResponseBody void postTweet(@PathVariable Long eventId, @RequestParam String status, @OAuthAccessToken("twitter") OAuthConsumerToken accessToken) {
		twitter.updateStatus(accessToken, status);
	}

	@RequestMapping(value="/{id}/sessions/favorites", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> favoriteSessions(@PathVariable Long id, Account account) {
		return eventRepository.findFavoriteSessions(id, account.getId());
	}

	@RequestMapping(value="/{id}/sessions/today", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> sessionsToday(@PathVariable Long id) {
		return eventRepository.findTodaysSessions(id);
	}

	@RequestMapping(value="/{id}/sessions/{day}", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> sessionsOnDay(@PathVariable Long id, @PathVariable @DateTimeFormat(iso=ISO.DATE) LocalDate day) {
		return eventRepository.findSessionsOnDay(id, day);
	}

	@RequestMapping(value="/{id}/sessions/{number}/favorite", method=RequestMethod.PUT)
	public @ResponseBody Boolean toggleFavorite(@PathVariable Long id, @PathVariable Short number, Account account) {
		return eventRepository.toggleFavorite(id, number, account.getId());
	}

	@RequestMapping(value="/{id}/sessions/{number}/rating", method=RequestMethod.PUT)
	public @ResponseBody void updateRating(@PathVariable Long id, @PathVariable Short number, Account account, @RequestParam Short value) {
		eventRepository.updateRating(id, number, account.getId(), value);
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