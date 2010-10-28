package com.springsource.greenhouse.home;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.activity.recent.RecentActivityRepository;

@Controller
public class HomeController {

	private RecentActivityRepository recentActivityRepository;
	
	@Inject
	public HomeController(RecentActivityRepository recentActivityRepository) {
		this.recentActivityRepository = recentActivityRepository;
	}

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String home(Principal user, Model model) {
		if (user == null) {
			model.addAttribute("recentActivity", recentActivityRepository.findInitial());
			return "homeNotSignedIn";
		} else {
			return "homeSignedIn";
		}
	}
}
