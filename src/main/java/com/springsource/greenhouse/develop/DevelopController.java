package com.springsource.greenhouse.develop;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.account.Account;

@Controller
@RequestMapping("/develop")
public class DevelopController {

	private ConnectedAppRepository connectedAppService;

	@Inject
	public DevelopController(ConnectedAppRepository connectedAppService) {
		this.connectedAppService = connectedAppService;
	}

	@RequestMapping(value="/apps", method=RequestMethod.GET)
	public List<ConnectedAppSummary> appList(Account account) {
		return connectedAppService.findConnectedApps(account.getId());
	}

	@RequestMapping(value="/apps/{slug}", method=RequestMethod.GET)
	public ConnectedApp app(Account account, String slug) {
		return connectedAppService.findConnectedApp(account.getId(), slug);
	}

	@RequestMapping(value="/appform", method=RequestMethod.GET)
	public ConnectedAppForm connectedAppForm() {
		return new ConnectedAppForm();
	}
	
	@RequestMapping(value="/appform", method=RequestMethod.POST)
	public String post(@Valid ConnectedAppForm form, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return null;
		}
		String slug = connectedAppService.createConnectedApp(form);
		return "redirect:/develop/apps/" + slug;
	}

}
