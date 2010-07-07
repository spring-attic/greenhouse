package com.springsource.greenhouse;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.updates.GreenhouseUpdatesService;

@Controller
public class HomeController {

	private GreenhouseUpdatesService updatesService;
	
	@Inject
	public HomeController(GreenhouseUpdatesService updatesService) {
		this.updatesService = updatesService;
	}

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String home(Principal user, Model model) {
		if (user == null) {
			model.addAttribute("updates", updatesService.getUpdates());
			return "homeNotSignedIn";
		} else {
			return "homeSignedIn";
		}
	}
}
