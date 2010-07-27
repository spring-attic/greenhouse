package com.springsource.greenhouse.groups;

import org.springframework.social.twitter.TwitterOperations;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/groups")
public class GroupsController {

	private GroupRepository groupRepository;
	
	private TwitterOperations twitter;

	@RequestMapping(value="/{group}/{year}/{month}", method=RequestMethod.GET, headers="Accept=text/html")
	public String eventView(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, Model model) {
		GroupEvent event = groupRepository.findEventByMonthOfYear(group, month, year);
		model.addAttribute(event);
		model.addAttribute(twitter.search(event.getSearchString(), 1, 10));
		return "events/view";
	}
	
}