package com.springsource.greenhouse.events;

import javax.inject.Inject;

import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/groups")
public class GroupsController {
	private EventsService eventsService;
	private TwitterOperations twitter;
	
	@Inject
	public GroupsController(EventsService eventsService, TwitterOperations twitter) {
		this.eventsService = eventsService;
		this.twitter = twitter;
	}

	@RequestMapping(value="/{groupName}/events/{eventName}", method=RequestMethod.GET)
	public String viewGroupEvent(OAuthConsumerToken accessToken,
								 @PathVariable("groupName") String groupName, 
								 @PathVariable("eventName") String eventName,
								 Model model) {

		Event event = eventsService.findEventByGroupNameAndEventName(groupName, eventName);
		model.addAttribute(event);
		model.addAttribute(twitter.search(accessToken, event.getHashtag()));
		return "events/view";
	}
}
