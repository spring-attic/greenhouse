package com.springsource.greenhouse.groups;

import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/groups")
public class GroupsController {

	private GroupRepository groupRepository;
	
	private TwitterOperations twitter;

	public GroupsController(GroupRepository groupRepository, TwitterOperations twitter) {
		this.groupRepository = groupRepository;
		this.twitter = twitter;
	}

	@RequestMapping(value="/{group}/events/{year}/{month}", method=RequestMethod.GET, headers="Accept=text/html")
	public String eventView(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, Model model) {
		GroupEvent event = groupRepository.findEventByMonthOfYear(group, month, year);
		model.addAttribute(event);
		model.addAttribute(twitter.search(event.getSearchString(), 1, 10));
		return "groups/event";
	}
	
}