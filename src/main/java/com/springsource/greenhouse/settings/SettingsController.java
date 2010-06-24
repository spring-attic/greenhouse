package com.springsource.greenhouse.settings;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/settings")
public class SettingsController {

	@RequestMapping(method=RequestMethod.GET)
	public void settingsForm() {
		
	}
	
	@RequestMapping(value="/apps/{id}", method=RequestMethod.DELETE)
	public void removeAppLink(@PathVariable String id) {
		
	}
}
