package com.springsource.greenhouse.develop;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.account.Account;

@Controller
@RequestMapping("/develop")
public class DevelopController {

	private ConnectedAppRepository connectedAppRepository;

	@Inject
	public DevelopController(ConnectedAppRepository connectedAppService) {
		this.connectedAppRepository = connectedAppService;
	}

	@RequestMapping(value="/apps", method=RequestMethod.GET)
	public List<ConnectedAppSummary> appList(Account account) {
		return connectedAppRepository.findConnectedApps(account.getId());
	}

	@RequestMapping(value="/apps/new", method=RequestMethod.GET)
	public ConnectedAppForm getNewAppForm() {
		return connectedAppRepository.getNewAppForm();
	}

	@RequestMapping(value="/apps/new", method=RequestMethod.POST)
	public String postAppForm(@Valid ConnectedAppForm form, BindingResult bindingResult) {
		// TODO push this check back into the framework code
		if (bindingResult.hasErrors()) {
			return null;
		}
		String slug = connectedAppRepository.createConnectedApp(form);
		return "redirect:/develop/apps/" + slug;
	}

	@RequestMapping(value="/apps/{slug}", method=RequestMethod.GET)
	public String putAppForm(Account account, @PathVariable String slug, ConnectedAppForm form, Model model) {
		connectedAppRepository.updateConnectedApp(account.getId(), slug, form);
		return "redirect:/develop/apps/" + slug;
	}

	@RequestMapping(value="/apps/edit/{slug}", method=RequestMethod.GET)
	public ConnectedAppForm getEditAppForm(Account account, String slug) {
		return connectedAppRepository.getAppForm(account.getId(), slug);
	}

	@RequestMapping(value="/apps/edit/{slug}", method=RequestMethod.POST)
	public String postAppForm(Account account, @PathVariable String slug, @Valid ConnectedAppForm form, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return null;
		}
		slug = connectedAppRepository.updateConnectedApp(account.getId(), slug, form);
		return "redirect:/develop/apps/" + slug;
	}

	
	@RequestMapping(value="/apps/{slug}", method=RequestMethod.DELETE)
	public String deleteApp(Account account, @PathVariable String slug) {
		connectedAppRepository.deleteConnectedApp(account.getId(), slug);
		return "redirect:/develop/apps";
	}

}
