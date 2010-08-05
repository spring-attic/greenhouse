package com.springsource.greenhouse.groups;

import javax.inject.Inject;

import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.events.Event;
import com.springsource.greenhouse.events.EventRepository;

@Controller
@RequestMapping("/groups")
public class GroupsController {

	private EventRepository eventRepository;
	
	private TwitterOperations twitter;

	private GroupRepository groupRepository;

	@Inject
	public GroupsController(GroupRepository groupRepository, EventRepository eventRepository, TwitterOperations twitter) {
		this.groupRepository = groupRepository;
		this.eventRepository = eventRepository;
		this.twitter = twitter;
	}
	
	@RequestMapping(value="/{group}")
	public String groupView(@PathVariable String group, Model model) {
		model.addAttribute(groupRepository.findGroupByProfileKey(group));
		return "groups/view";
	}
	
	@RequestMapping(value="/{group}/events/{year}/{month}/{name}", method=RequestMethod.GET, headers="Accept=text/html")
	public String eventView(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, @PathVariable String name, Model model) {
		Event event = eventRepository.findEventByName(group, year, month, name);
		model.addAttribute(event);
		model.addAttribute(twitter.search(event.getHashtag(), 1, 10));
		return "groups/event";
	}	
}