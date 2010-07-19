package com.springsource.greenhouse.events;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.social.twitter.SearchResults;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.ui.ExtendedModelMap;

public class EventsControllerTest {
	private EventsController eventsController;
	private Event expectedEvent;
	private SearchResults expectedResults;

	@Before
	public void setup() {
		EventsService eventsService = mock(EventsService.class);
		expectedEvent = new Event();
		expectedEvent.setHashtag("#testevent");
		when(eventsService.findEventByPublicId("test_event")).thenReturn(expectedEvent);
		when(eventsService.findEventById(anyInt())).thenReturn(expectedEvent);
		
		TwitterOperations twitter = mock(TwitterOperations.class);
		expectedResults = new SearchResults();
		when(twitter.search(any(OAuthConsumerToken.class), eq("#testevent"), anyInt(), anyInt()))
			.thenReturn(expectedResults);
		
		eventsController = new EventsController(eventsService, twitter);
	}
	
	@Test
	public void shouldAddEventAndTweetsToModelWhenViewingEvent() {
		ExtendedModelMap model = new ExtendedModelMap();
		assertEquals("events/view", eventsController.viewEvent(new OAuthConsumerToken(), "test_event", model));
		assertSame(expectedEvent, model.get("event"));
		assertSame(expectedResults, model.get("searchResults"));
	}
	
	@Test
	public void shouldReturnSearchResultsForAnEvent() {
		SearchResults actualResults = eventsController.listEventTweets(new OAuthConsumerToken(), 42, 1, 10);
		assertSame(expectedResults, actualResults);
	}
}
