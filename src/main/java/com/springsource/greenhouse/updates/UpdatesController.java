package com.springsource.greenhouse.updates;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/updates")
public class UpdatesController {
	
	private GreenhouseUpdatesService updatesService;
	
	@Inject
	public UpdatesController(GreenhouseUpdatesService updatesService) {
		this.updatesService = updatesService;
	}
	
	@RequestMapping(method=RequestMethod.GET, headers="Accept=application/json") 
	public @ResponseBody List<Update> updatesData() {
		return updatesService.getUpdates();
	}

	@RequestMapping(method=RequestMethod.GET)
	public void updates(Model model) {
		List<Update> updates = updatesService.getUpdates();
		model.addAttribute("updates", updates);
	}
}
