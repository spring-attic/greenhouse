/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.events;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.twitter.TwitterApi;
import org.springframework.social.twitter.types.SearchResults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.events.EventForm;
import com.springsource.greenhouse.utils.Location;
import com.springsource.greenhouse.events.EventRepository;
import com.springsource.greenhouse.events.JdbcEventRepository;

/**
 * UI Controller for Event actions.
 * @author Keith Donald
 */
@Controller
public class EventsController {
	
	private final EventRepository eventRepository;
	
	private final TwitterApi twitterApi;
		
	@Inject
	public EventsController(EventRepository eventRepository, TwitterApi twitterApi) {
		this.eventRepository = eventRepository;
		this.twitterApi = twitterApi;
	}
	
	// for web service (JSON) clients
	/**
	 * Write the list of upcoming events to the body of the response.
	 * Only matches 'GET /events' requests for JSON content; a 404 is sent otherwise.
	 * TODO send a 406 if an unsupported representation, such as XML, is requested.  See SPR-7353.
	 */
	@RequestMapping(value="/events", method=RequestMethod.GET, headers="Accept=application/json") 
	public @ResponseBody List<Event> upcomingEvents(@RequestParam(value="after", required=false) @DateTimeFormat(iso=ISO.DATE_TIME) Long afterMillis) {
		return eventRepository.findUpcomingEvents(afterMillis);
	}

	/**
	 * Write the list of event favorites to the body of the response.
	 */
	@RequestMapping(value="/events/{eventId}/favorites", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> favorites(@PathVariable Long eventId, Account account) {
		return eventRepository.findEventFavorites(eventId, account.getId());
	}

	/**
	 * Write a page of event tweet search results to the body of the response.
	 * The page number and size may be provided by the client.  If not specified, defaults to the first page of ten results.
	 */
	@RequestMapping(value="/events/{eventId}/tweets", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody SearchResults tweets(@PathVariable Long eventId,  @RequestParam(defaultValue="1") Integer page, @RequestParam(defaultValue="10") Integer pageSize) {
		String searchString = eventRepository.findEventSearchString(eventId);
		return searchString != null && searchString.length() > 0 ? twitterApi.searchOperations().search(searchString, page, pageSize) : null;
	}

	/**
	 * Post a tweet about the event to Twitter.
	 * Write OK status back if this is successful.
	 */
	@RequestMapping(value="/events/{eventId}/tweets", method=RequestMethod.POST)
	public ResponseEntity<String> postTweet(@PathVariable Long eventId, @RequestParam String status, Location currentLocation) {
		twitterApi.timelineOperations().updateStatus(status);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	/**
	 * Retweet another event tweet.
	 */
	@RequestMapping(value="/events/{eventId}/retweet", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> postRetweet(@PathVariable Long eventId, @RequestParam Long tweetId) {
		twitterApi.timelineOperations().retweet(tweetId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	/**
	 * Write the attendee's list of favorite sessions to the body of the response.
	 */
	@RequestMapping(value="/events/{eventId}/sessions/favorites", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> favoriteSessions(@PathVariable Long eventId, Account account) {
		return eventRepository.findAttendeeFavorites(eventId, account.getId());
	}

	/**
	 * Write the sessions scheduled for the day to the body of the response.
	 */
	@RequestMapping(value="/events/{eventId}/sessions/{day}", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> sessionsOnDay(@PathVariable Long eventId, @PathVariable @DateTimeFormat(iso=ISO.DATE) LocalDate day, Account account) {
		return eventRepository.findSessionsOnDay(eventId, day, account.getId());
	}

	/**
	 * Toggle a session as an attendee favorite.
	 * Write the new favorite status to the body of the response.
	 */
	@RequestMapping(value="/events/{eventId}/sessions/{sessionId}/favorite", method=RequestMethod.PUT)
	public @ResponseBody Boolean toggleFavorite(@PathVariable Long eventId, @PathVariable Integer sessionId, Account account) {
		return eventRepository.toggleFavorite(eventId, sessionId, account.getId());
	}

	/**
	 * Add or update the rating given to the session by the attendee.
	 * Write the new average rating for the session to the body of the response.
	 */
	@RequestMapping(value="/events/{eventId}/sessions/{sessionId}/rating", method=RequestMethod.POST)
	public @ResponseBody Float updateRating(@PathVariable Long eventId, @PathVariable Integer sessionId, Account account, @RequestParam Short value, @RequestParam String comment) throws RatingPeriodClosedException {
		return eventRepository.rate(eventId, sessionId, account.getId(), new Rating(value, comment));
	}

	/**
	 * Write a page of session tweet search results to the body of the response.
	 */
	@RequestMapping(value="/events/{eventId}/sessions/{sessionId}/tweets", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody SearchResults sessionTweets(@PathVariable Long eventId, @PathVariable Integer sessionId, @RequestParam(defaultValue="1") Integer page, @RequestParam(defaultValue="10") Integer pageSize) {
		String searchString = eventRepository.findSessionSearchString(eventId, sessionId);
		return searchString != null && searchString.length() > 0 ? twitterApi.searchOperations().search(searchString, page, pageSize) : null;
	}

	/**
	 * Post a tweet about a session.
	 */
	@RequestMapping(value="/events/{eventId}/sessions/{sessionId}/tweets", method=RequestMethod.POST)
	public ResponseEntity<String> postSessionTweet(@PathVariable Long eventId, @PathVariable Integer sessionId, @RequestParam String status, Location currentLocation) {
		twitterApi.timelineOperations().updateStatus(status);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	/**
	 * Retweet a session tweet.
	 */
	@RequestMapping(value="/events/{eventId}/sessions/{sessionId}/retweet", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> postSessionRetweet(@PathVariable Long eventId, @PathVariable Integer sessionId, @RequestParam Long tweetId) {
		twitterApi.timelineOperations().retweet(tweetId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	// for web browser (HTML) clients

	/**
	 * Render the list of upcoming events as HTML in the client's web browser. 
	 */
	@RequestMapping(value="/events", method=RequestMethod.GET, headers="Accept=text/html")
	public String upcomingEventsView(Model model, DateTimeZone timeZone) {
		model.addAttribute(eventRepository.findUpcomingEvents(new DateTime(timeZone).getMillis()));
		return "events/list";
	}
	/**
	 *  Renders the event form for the user.
	 */
	@RequestMapping(value="/events/new", method=RequestMethod.GET) 
	public EventForm NewForm(Model model) { 
		Object timezoneList[] = DateTimeZone.getAvailableIDs().toArray();
		model.addAttribute("timezoneList", timezoneList); 
		String venueList[] = eventRepository.selectVenueNames();
		model.addAttribute("venueList", venueList);
		String addressList[] = eventRepository.selectVenueAddresses();
		model.addAttribute("addressList", addressList);
		String hintsList[] = eventRepository.selectVenueLocationHints();
		model.addAttribute("hintsList", hintsList);
		return eventRepository.getNewEventForm(); 
	}
	
	/**
	* Register a new Event for the developer.
	 * @throws IOException 
	*/
	@RequestMapping(value="/events", method=RequestMethod.POST)
	public String create(@Valid EventForm form, BindingResult bindingResult, Account account, Model model) throws IOException {
	if (bindingResult.hasErrors()) {
		Object timezoneList[] = DateTimeZone.getAvailableIDs().toArray();
		model.addAttribute("timezoneList", timezoneList); 
		String venueList[] = eventRepository.selectVenueNames();
		model.addAttribute("venueList", venueList);
		String addressList[] = eventRepository.selectVenueAddresses();
		model.addAttribute("addressList", addressList);
		String hintsList[] = eventRepository.selectVenueLocationHints();
		model.addAttribute("hintsList", hintsList);
		return "events/new";
	}
	
	eventRepository.createEvent(account.getId(), form);
	return "redirect:/events";
	}

	@RequestMapping(value="/groups/NewSession", method=RequestMethod.GET) 
	public EventSessionForm NewSessionForm(Model model) {
		
		return eventRepository.getNewSessionForm();
		
	}
	
	@RequestMapping(value="/groups/NewSession", method=RequestMethod.POST) 
	public String create (@Valid EventSessionForm form, BindingResult bindingResult, Account account, Model model)throws IOException {
	return "groups/NewSession";
	}
	
	
	}

	
