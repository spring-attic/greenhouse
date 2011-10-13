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
package com.springsource.greenhouse.groups;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.events.Event;
import com.springsource.greenhouse.events.EventRepository;

/**
 * UI Controller for managing groups.
 * @author Keith Donald
 */
@Controller
public class GroupsController {
	
	private GroupRepository groupRepository;

	private EventRepository eventRepository;	
	
	@Inject
	public GroupsController(GroupRepository groupRepository, EventRepository eventRepository) {
		this.groupRepository = groupRepository;
		this.eventRepository = eventRepository;
	}
	
	/**
	 * Render the details of a group as HTML in the web browser.
	 */
	@RequestMapping(value="/groups/{groupKey}")
	public String groupView(@PathVariable String groupKey, Model model) {
		Group group = groupRepository.findGroupBySlug(groupKey);
		model.addAttribute(group);		
		model.addAttribute("metadata", buildOpenGraphMetadata(group));
		model.addAttribute("facebookAppId", facebookAppId);
		return "groups/view";
	}

	/**
	 * Render the details of an event organized by the group as HTML in the web browser.
	 */
	@RequestMapping(value="/groups/{group}/events/{year}/{month}/{slug}", method=RequestMethod.GET)
	public String eventView(@PathVariable String group, @PathVariable Integer year, @PathVariable Integer month, @PathVariable String slug, Model model) {
		Event event = eventRepository.findEventBySlug(group, year, month, slug);
		model.addAttribute(event);
		return "groups/event";
	}	

	// internal helpers
	
	// this metadata is required by Facebook's "Like" widgets and included in the page by meta tags in page header
	private Map<String, String> buildOpenGraphMetadata(Group group) {
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("og:title", group.getName());
		 // TODO: "non-profit" is hardcoded		
		metadata.put("og:type", "non_profit");
		// TODO Greenhouse is hardcoded here		
		metadata.put("og:site_name", "Greenhouse");
		metadata.put("fb:app_id", facebookAppId);
		return metadata;		
	}
	
	@Value("#{environment['facebook.appId']}")
	private String facebookAppId;

}