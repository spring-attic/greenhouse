package com.springsource.greenhouse.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Inject
	public EventsController(EventsService eventsService, TwitterOperations twitter) {
		this.eventsService = eventsService;
		this.twitter = twitter;
	}
	
	@RequestMapping(method=RequestMethod.GET, headers="Accept=application/json") 
	public @ResponseBody List<Event> eventsData() {
		return eventsService.findEventsAfter(new Date());
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String listEvents(Model model) {
		model.addAttribute(eventsService.findEventsAfter(new Date()));
		return "events/list";
	}

	@RequestMapping(value="/{eventId}", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody Event eventData(@PathVariable long eventId) {
		return eventsService.findEventById(eventId);
	}

	@RequestMapping(value="/{eventName}", method=RequestMethod.GET)
	public String viewEvent(OAuthConsumerToken accessToken, @PathVariable String eventName, Model model) {
		Event event = eventsService.findEventByPublicId(eventName);
		model.addAttribute(event);
		model.addAttribute(twitter.search(accessToken, event.getHashtag(), 1, 10));
		return "events/view";
	}
	
	@RequestMapping(value="/{eventId}/tweets", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody SearchResults listEventTweets(OAuthConsumerToken accessToken, @PathVariable long eventId, 
			@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int perPage) {
		Event event = eventsService.findEventById(eventId);
		return twitter.search(accessToken, event.getHashtag(), page, perPage);
	}
	
	// This method is stubbed out to test posting a twitter status
	@RequestMapping(value="/tweet", method=RequestMethod.POST)//, headers="Accept=application/json")
	public @ResponseBody String postTweet(OAuthConsumerToken accessToken, @RequestParam String status) {
		logger.info("twitter update: " + status);
		
		return "hello";
	}
		
	// This method should return all sessions for the current day
	@RequestMapping(value="/{eventId}/currentsessions", method=RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody List<EventSession> listCurrentSessions(@PathVariable long eventId) {
		
		// This is just a bunch of test session data that updates to the current date so 
		// we can always see upcoming and happening now sessions.
		
		List<EventSession> sessions = new ArrayList<EventSession>();
		
		Date now = new Date();
		int year = now.getYear() + 1900;
		int month = now.getMonth() + 1;
		int date = now.getDate();
		
		Date startTime = new DateTime(year, month, date, 8, 30, 0, 0).toDate();
		Date endTime = new DateTime(year, month, date, 10, 0, 0, 0).toDate();
		
		for (int i = 0; i < 9; i++)
		{
			sessions.add(new EventSession("Test Session A" + i, "Description for Test Session A" + i, startTime, endTime, new SessionLeader("Johnny", "Speaker"), "#SpringOne2GX-A" + i));
		}

		startTime = new DateTime(year, month, date, 10, 15, 0, 0).toDate();
		endTime = new DateTime(year, month, date, 11, 45, 0, 0).toDate();
		
		for (int i = 0; i < 9; i++)
		{
			List<SessionLeader> leaders = new ArrayList<SessionLeader>();
			leaders.add(new SessionLeader("Roy", "Clarkson"));
			leaders.add(new SessionLeader("Jeremy", "Grelle"));
			sessions.add(new EventSession("Test Session B" + i, "Description for Test Session B" + i, startTime, endTime, leaders, "#SpringOne2GX-B" + i));
		}

		startTime = new DateTime(year, month, date, 12, 45, 0, 0).toDate();
		endTime = new DateTime(year, month, date, 14, 15, 0, 0).toDate();
		
		for (int i = 0; i < 9; i++)
		{
			sessions.add(new EventSession("Test Session C" + i, "Description for Test Session C" + i, startTime, endTime, new SessionLeader("Johnny", "Speaker"), "#SpringOne2GX-C" + i));
		}

		startTime = new DateTime(year, month, date, 14, 45, 0, 0).toDate();
		endTime = new DateTime(year, month, date, 16, 15, 0, 0).toDate();

		for (int i = 0; i < 9; i++)
		{
			sessions.add(new EventSession("Test Session D" + i, "Description for Test Session D" + i, startTime, endTime, new SessionLeader("Johnny", "Speaker"), "#SpringOne2GX-D" + i));
		}

		startTime = new DateTime(year, month, date, 16, 30, 0, 0).toDate();
		endTime = new DateTime(year, month, date, 18, 0, 0, 0).toDate();

		for (int i = 0; i < 9; i++)
		{
			sessions.add(new EventSession("Test Session E" + i, "Description for Test Session E" + i, startTime, endTime, new SessionLeader("Johnny", "Speaker"), "#SpringOne2GX-E" + i));
		}
				
//		sessions.add(new EventSession("What's new in Spring 3", "Come see the latest Spring Framework features!", new DateTime(2010, 10, 20, 8, 0, 0, 0).toDate(), new DateTime(2010, 10, 20, 9, 0, 0, 0).toDate(), new SessionLeader("Juergen", "Hoeller")));
//		sessions.add(new EventSession("What's new in Grails 2", "Come see the latest Grails features!", new DateTime(2010, 10, 20, 8, 0, 0, 0).toDate(), new DateTime(2010, 10, 20, 9, 0, 0, 0).toDate(), new SessionLeader("Graeme", "Rocher")));
//		sessions.add(new EventSession("Building Social Ready Webapps", "Come learn how to do the social stuff!", new DateTime(2010, 10, 20, 9, 0, 0, 0).toDate(), new DateTime(2010, 10, 20, 10, 0, 0, 0).toDate(), new SessionLeader("Craig", "Walls")));
//		
//		List<SessionLeader> mobileLeaders = new ArrayList<SessionLeader>();
//		mobileLeaders.add(new SessionLeader("Roy", "Clarkson"));
//		mobileLeaders.add(new SessionLeader("Jeremy", "Clarkson"));
//		sessions.add(new EventSession("Choices in Mobile Application Development", "Come learn how to do the mobile stuff!", new DateTime(2010, 10, 20, 9, 0, 0, 0).toDate(), new DateTime(2010, 10, 20, 10, 0, 0, 0).toDate(), mobileLeaders));
		
		return sessions;
	}
}
