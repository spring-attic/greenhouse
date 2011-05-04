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
package com.springsource.greenhouse.activity.recent;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Serves up the most recent set of recent activity items for display to the user.
 * @author Keith Donald
 */
@Controller
public class RecentActivityController {
	
	private final RecentActivityRepository repository;
	
	@Inject
	public RecentActivityController(RecentActivityRepository repository) {
		this.repository = repository;
	}
	
	@RequestMapping(value="/recent", method=RequestMethod.GET, headers="Accept=application/json") 
	public @ResponseBody RecentActivity next(@RequestParam @DateTimeFormat(iso=ISO.DATE_TIME) DateTime last) {
		return repository.findNext(last);
	}

}