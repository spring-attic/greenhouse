package com.springsource.greenhouse.groups;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${facebook.applicationId}")
	private String facebookAppId; 

	private EventRepository eventRepository;	
	
	private TwitterOperations twitter;
	
	private GroupRepository groupRepository;

	@Inject
	public GroupsController(GroupRepository groupRepository, EventRepository eventRepository, TwitterOperations twitter) {
		this.groupRepository = groupRepository;
		this.eventRepository = eventRepository;
		this.twitter = twitter;
	}
	
	@RequestMapping(value="/{groupKey}")
	public String groupView(@PathVariable String groupKey, Model model) {
		Group group = groupRepository.findGroupByProfileKey(groupKey);
		model.addAttribute(group);		
		model.addAttribute("metadata", buildOpenGraphMetadata(group));		
		return "groups/view";
	}
	
	@RequestMapping(value="/{group}/events/{year}/{month}/{name}", method=RequestMethod.GET, headers="Accept=text/html")
	public String eventView(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, @PathVariable String name, Model model) {
		Event event = eventRepository.findEventByName(group, year, month, name);
		model.addAttribute(event);
		model.addAttribute(twitter.search(event.getHashtag(), 1, 10));
		return "groups/event";
	}	
	
	private Map<String, String> buildOpenGraphMetadata(Group group) {
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("og:title", group.getName());
		metadata.put("og:type", "non_profit"); // TODO: Not sure if this applies to all groups.
		metadata.put("og:site_name", "Greenhouse");
		metadata.put("fb:app_id", facebookAppId);
		return metadata;		
	}
}