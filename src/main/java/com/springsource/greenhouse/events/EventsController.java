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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.TwitterApi;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.Location;

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
	public @ResponseBody ResponseEntity<String> postRetweet(@PathVariable Long eventId, @RequestParam Long tweetId) {
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
	public @ResponseBody ResponseEntity<String> postSessionRetweet(@PathVariable Long eventId, @PathVariable Integer sessionId, @RequestParam Long tweetId) {
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
	public EventForm newForm(Model model) { 
		populateEventFormModel(model);
		return new EventForm();
	}

	/**
	* Registers a new Event for the user.
	 * @throws IOException 
	*/
	@RequestMapping(value="/events", method=RequestMethod.POST)
	public String create(@Valid EventForm form, BindingResult bindingResult, Account account, Model model) throws IOException {
		if (bindingResult.hasErrors()) {
			populateEventFormModel(model);
			return "events/new";
		}
		if (form.getStartTime().getMillis() >= form.getEndTime().getMillis()){
			bindingResult.rejectValue("startDate", "start date must be before end date", "start date must be before end date");
			return "events/new";
		}
		eventRepository.createEvent(form, account.getId());
		return "redirect:/events";
	}

	@RequestMapping(value="/groups/{group}/events/{year}/{month}/{slug}/rooms/new", method=RequestMethod.GET, headers="Accept=text/html") 
	public String newRoomForm(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, @PathVariable String slug, Account account, Model model) {
		model.addAttribute(new EventRoomForm());
		Event event = eventRepository.findEventBySlug(group, year, month, slug);
		model.addAttribute(event);
		return "groups/event/newRoom";
	}
		
	
	@RequestMapping(value="/groups/{group}/events/{year}/{month}/{slug}/rooms", method=RequestMethod.POST) 
	public String createRoom(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, @PathVariable String slug, @Valid EventRoomForm form, BindingResult bindingResult, Account account, Model model){
		if (bindingResult.hasErrors()) {
			Event event = eventRepository.findEventBySlug(group, year, month, slug);
			model.addAttribute(event);
			return "groups/event/newRoom";
		}
		Event event = eventRepository.findEventBySlug(group, year, month, slug);
		eventRepository.createRoom(account.getId(), event, form);
		return "redirect:/groups/" + group + "/events/" + year + "/" + month + "/" + slug;
	}
	
	
	@RequestMapping(value="/groups/{group}/events/{year}/{month}/{slug}/sessions/new", method=RequestMethod.GET, headers="Accept=text/html")
	public String newSessionForm(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, @PathVariable String slug, Account account, Model model){
		model.addAttribute(new EventSessionForm());
		populateSessionFormModel(group, year, month, slug, model);
		return "groups/event/newSession";
	}
	
	@RequestMapping(value="/groups/{group}/events/{year}/{month}/{slug}/sessions", method=RequestMethod.POST) 
	public String createSession(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, @PathVariable String slug, @Valid EventSessionForm form, BindingResult bindingResult, Account account, Model model){
		if (bindingResult.hasErrors()) {
			populateSessionFormModel(group, year, month, slug, model);
			return "groups/event/newSession";
		}
		// TODO move the event query and validation check into the repository in a transaction
		Event event = eventRepository.findEventBySlug(group, year, month, slug);
		if (form.getStartTime(event.getTimeZone()).getMillis() >= form.getEndTime(event.getTimeZone()).getMillis()) {
			bindingResult.rejectValue("endDate", "start date must be before end date", "start date must be before end date");
			populateSessionFormModel(group, year, month, slug, model);
			return "groups/event/newSession";
		}
		eventRepository.createSession(account.getId(), event, form);
		return "redirect:/groups/" + event.getGroupSlug() + "/events/" + event.getStartTime().getYear() + "/" + event.getStartTime().getMonthOfYear() + "/" + event.getSlug();
	 }

	@RequestMapping(value="/groups/{group}/events/{year}/{month}/{slug}/tracks/new", method=RequestMethod.GET, headers="Accept=text/html") 
	public String newTrackForm(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, @PathVariable String slug, Account account, Model model) {
		model.addAttribute(new EventTrackForm());
		Event event = eventRepository.findEventBySlug(group, year, month, slug);
		model.addAttribute(event);
		return "groups/event/newTrack";
	}
	
	@RequestMapping(value="/groups/{group}/events/{year}/{month}/{slug}/tracks", method=RequestMethod.POST) 
	public String createTrack(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, @PathVariable String slug, @Valid EventTrackForm form, BindingResult bindingResult, Account account, Model model) {
		if (bindingResult.hasErrors()) {
			Event event = eventRepository.findEventBySlug(group, year, month, slug);
			model.addAttribute(event);
			return "groups/event/newTrack";
		}
		Event event = eventRepository.findEventBySlug(group, year, month, slug);
		if (form.getCode().isEmpty()){ //this is necessary because @NotEmpty cannot be added to form if the edit form doesnt allow user to edit the code once it is made
			bindingResult.rejectValue("code", "code must be entered", "code must be entered");
			model.addAttribute(event);
			return "groups/event/newTrack";
		}
		try {
			eventRepository.createTrack(account.getId(), event, form);
		} catch (DuplicateKeyException e){
			bindingResult.rejectValue("code", "code already exists for this event", "code already exists for this event");
			model.addAttribute(event);
			return "groups/event/newTrack";
		}
		return "redirect:/groups/" + group + "/events/" + year + "/" + month + "/" + slug;
	}
	
	@RequestMapping(value="/groups/{group}/events/{year}/{month}/{slug}/tracks/{trackcode}", method=RequestMethod.GET, headers="Accept=text/html") 
	public String viewTrack(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, @PathVariable String slug, @PathVariable String trackcode, Account account, Model model) {
		Event event = eventRepository.findEventBySlug(group, year, month, slug);
		EventTrack track = eventRepository.findTrackByCode(trackcode, event.getId());
		model.addAttribute("track", track);
		model.addAttribute("event", event);
		return "groups/event/track";
	}

	@RequestMapping(value="/groups/{group}/events/{year}/{month}/{slug}/sessions/{sessionid}", method=RequestMethod.GET, headers="Accept=text/html") 
	public String viewTrack(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, @PathVariable String slug, @PathVariable Integer sessionid, Account account, Model model) {
		Event event = eventRepository.findEventBySlug(group, year, month, slug);
		EventSession session = eventRepository.findSessionById(sessionid, event.getId());
		String roomName = session.getRoom().getLabel();
		model.addAttribute("event", event);
		model.addAttribute("session", session);
		model.addAttribute("roomName", roomName);
		return "groups/event/session";
	}
	
	@RequestMapping(value="/greenhouse/groups/{group}/events/{year}/{month}/{slug}/tracks/edit/{trackcode}", method=RequestMethod.GET, headers="Accept=text/html")
	public String editTrackForm(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, @PathVariable String slug, @PathVariable String trackcode, Account account, Model model) {
		Event event = eventRepository.findEventBySlug(group, year, month, slug);
		model.addAttribute(eventRepository.getTrackForm(event.getId(), trackcode));
		String roomList[] = eventRepository.selectRoomNames(event.getVenues().iterator().next().getId());
		model.addAttribute("roomList", roomList);
		model.addAttribute("event", event);
		model.addAttribute("trackcode", trackcode);
		return "groups/event/track/edit";
	}
	
	@RequestMapping(value="/groups/{group}/events/{year}/{month}/{slug}/tracks/{trackcode}", method=RequestMethod.PUT)
	public String updateTrack(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, @PathVariable String slug, @PathVariable String trackcode, @Valid EventTrackForm form, BindingResult bindingResult, Account account, Model model) {
		if (bindingResult.hasErrors()) {
			Event event = eventRepository.findEventBySlug(group, year, month, slug);
			model.addAttribute(event);			
			return "groups/event/track/edit";
		}
		Event event = eventRepository.findEventBySlug(group, year, month, slug);
		eventRepository.updateTrack(event, form, trackcode);
		return "redirect:/groups/" + group + "/events/" + year + "/" + month + "/" + slug + "/tracks/" + trackcode;
	}
	
	// internal helpers
	
	private void populateEventFormModel(Model model) {
		model.addAttribute("timeZones", DateTimeZone.getAvailableIDs());
		// TODO combine this into one structure
		String venueList[] = eventRepository.selectVenueNames();
		model.addAttribute("venueList", venueList);
		String addressList[] = eventRepository.selectVenueAddresses();
		model.addAttribute("addressList", addressList);
		String hintsList[] = eventRepository.selectVenueLocationHints();
		model.addAttribute("hintsList", hintsList);
	}
	
	private void populateSessionFormModel(String group, Integer year, Integer month, String slug, Model model) {
		String speakerList[] = eventRepository.selectSpeakerNames();
		model.addAttribute("speakerList", speakerList);
		Event event = eventRepository.findEventBySlug(group, year, month, slug);
		model.addAttribute(event);
		List<EventTrack> trackList = eventRepository.selectEventTracks(event.getId());
		model.addAttribute("trackList", trackList);
	}

}