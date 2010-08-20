package com.springsource.greenhouse.events;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.oauth.extras.OAuthAccessToken;
import org.springframework.social.oauth.SimpleAccessTokenProvider;
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
import com.springsource.greenhouse.action.Location;

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
	public @ResponseBody List<EventSession> favorites(@PathVariable Long id, Account account) {
		return eventRepository.findFavorites(id, account.getId());
	}
	
	@RequestMapping(value="/{id}/tweets", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody SearchResults tweets(@PathVariable Long id,  @RequestParam(defaultValue="1") Integer page, @RequestParam(defaultValue="10") Integer pageSize) {
		return twitter.search(eventRepository.findEventSearchString(id), page, pageSize);
	}

	@RequestMapping(value="/{id}/tweets", method=RequestMethod.POST)
	public ResponseEntity<String> postTweet(@PathVariable Long id, @RequestParam String status, Location currentLocation, @OAuthAccessToken("Twitter") OAuthConsumerToken accessToken) {
		if (accessToken == null) {
			return new ResponseEntity<String>("Account not connected to Twitter", HttpStatus.PRECONDITION_FAILED);
		}
		twitter.tweet(status, new SimpleAccessTokenProvider<OAuthConsumerToken>(accessToken));
		return new ResponseEntity<String>((String) null, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}/retweet", method=RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> postRetweet(@PathVariable Long id, @RequestParam Long tweetId,
			@OAuthAccessToken("Twitter") OAuthConsumerToken accessToken) {
		if (accessToken == null) {
			return new ResponseEntity<String>("Account not connected to Twitter", HttpStatus.PRECONDITION_FAILED);
		}
		twitter.retweet(tweetId, new SimpleAccessTokenProvider<OAuthConsumerToken>(accessToken));
		return new ResponseEntity<String>((String) null, HttpStatus.OK);
	}

	@RequestMapping(value="/{id}/sessions/favorites", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> favoriteSessions(@PathVariable Long id, Account account) {
		return eventRepository.findAttendeeFavorites(id, account.getId());
	}

	@RequestMapping(value="/{id}/sessions/today", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> sessionsToday(@PathVariable Long id, Account account) {
		return eventRepository.findTodaysSessions(id, account.getId());
	}

	@RequestMapping(value="/{id}/sessions/{day}", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> sessionsOnDay(@PathVariable Long id, @PathVariable @DateTimeFormat(iso=ISO.DATE) LocalDate day, Account account) {
		return eventRepository.findSessionsOnDay(id, day, account.getId());
	}

	@RequestMapping(value="/{id}/sessions/{number}/favorite", method=RequestMethod.PUT)
	public @ResponseBody Boolean toggleFavorite(@PathVariable Long id, @PathVariable Short number, Account account) {
		return eventRepository.toggleFavorite(id, number, account.getId());
	}

	@RequestMapping(value="/{id}/sessions/{number}/rating", method=RequestMethod.POST)
	public @ResponseBody void updateRating(@PathVariable Long id, @PathVariable Short number, Account account, @RequestParam Short value, @RequestParam String comment) {
		eventRepository.rate(id, number, account.getId(), value, comment);
	}

	@RequestMapping(value="/{id}/sessions/{number}/tweets", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody SearchResults sessionTweets(@PathVariable Long id, @PathVariable Short number, @RequestParam(defaultValue="1") Integer page, @RequestParam(defaultValue="10") Integer pageSize) {
		return twitter.search(eventRepository.findSessionSearchString(id, number), page, pageSize);
	}

	@RequestMapping(value="/{id}/sessions/{number}/tweets", method=RequestMethod.POST)
	public ResponseEntity<String> postSessionTweet(@PathVariable Long id, @PathVariable Short number, @RequestParam String status, Location currentLocation, @OAuthAccessToken("Twitter") OAuthConsumerToken accessToken) {
		if (accessToken == null) {
			return new ResponseEntity<String>("Account not connected to Twitter", HttpStatus.PRECONDITION_FAILED);
		}		
		twitter.tweet(status, new SimpleAccessTokenProvider<OAuthConsumerToken>(accessToken));
		return new ResponseEntity<String>((String) null, HttpStatus.OK);		
	}
	
	@RequestMapping(value="/{id}/sessions/{number}/retweet", method=RequestMethod.POST)
	public @ResponseBody void postSessionRetweet(@PathVariable Long id, @RequestParam Long tweetId, @OAuthAccessToken("Twitter") OAuthConsumerToken accessToken) {
		//TODO: add controller logic for retweeting about a session
	}
	
	// for web browser (HTML) clients
	
	@RequestMapping(method=RequestMethod.GET, headers="Accept=text/html")
	public String upcomingEventsView(Model model) {
		model.addAttribute(eventRepository.findUpcomingEvents());
		return "events/list";
	}
	
}