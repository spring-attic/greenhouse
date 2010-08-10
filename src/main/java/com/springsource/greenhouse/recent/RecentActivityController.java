package com.springsource.greenhouse.recent;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/recent")
public class RecentActivityController {
	
	private RecentActivityRepository repository;
	
	@Inject
	public RecentActivityController(RecentActivityRepository repository) {
		this.repository = repository;
	}
	
	@RequestMapping(method=RequestMethod.GET, headers="Accept=application/json") 
	public @ResponseBody RecentActivity next(@RequestParam @DateTimeFormat(iso=ISO.DATE_TIME) DateTime last) {
		return repository.findNext(last);
	}

}