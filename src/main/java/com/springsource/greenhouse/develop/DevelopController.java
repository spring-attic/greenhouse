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

	private AppRepository connectedAppRepository;

	@Inject
	public DevelopController(AppRepository connectedAppService) {
		this.connectedAppRepository = connectedAppService;
	}

	@RequestMapping(value="/apps", method=RequestMethod.GET)
	public List<AppSummary> appList(Account account) {
		return connectedAppRepository.findAppSummaries(account.getId());
	}

	@RequestMapping(value="/apps/new", method=RequestMethod.GET)
	public AppForm getNewAppForm() {
		return connectedAppRepository.getNewAppForm();
	}

	@RequestMapping(value="/apps/new", method=RequestMethod.POST)
	public String postAppForm(Account account, @Valid AppForm form, BindingResult bindingResult) {
		// TODO push this check back into the framework code
		if (bindingResult.hasErrors()) {
			return null;
		}
		String slug = connectedAppRepository.createApp(account.getId(), form);
		return "redirect:/develop/apps/" + slug;
	}

	@RequestMapping(value="/apps/{slug}", method=RequestMethod.GET)
	public String putAppForm(Account account, @PathVariable String slug, AppForm form, Model model) {
		connectedAppRepository.updateApp(account.getId(), slug, form);
		return "redirect:/develop/apps/" + slug;
	}

	@RequestMapping(value="/apps/edit/{slug}", method=RequestMethod.GET)
	public AppForm getEditAppForm(Account account, String slug) {
		return connectedAppRepository.getAppForm(account.getId(), slug);
	}

	@RequestMapping(value="/apps/edit/{slug}", method=RequestMethod.POST)
	public String postAppForm(Account account, @PathVariable String slug, @Valid AppForm form, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return null;
		}
		slug = connectedAppRepository.updateApp(account.getId(), slug, form);
		return "redirect:/develop/apps/" + slug;
	}

	
	@RequestMapping(value="/apps/{slug}", method=RequestMethod.DELETE)
	public String deleteApp(Account account, @PathVariable String slug) {
		connectedAppRepository.deleteApp(account.getId(), slug);
		return "redirect:/develop/apps";
	}

}
