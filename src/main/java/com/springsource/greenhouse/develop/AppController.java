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
@RequestMapping("/develop/apps")
public class AppController {

	private AppRepository connectedAppRepository;

	@Inject
	public AppController(AppRepository connectedAppService) {
		this.connectedAppRepository = connectedAppService;
	}

	@RequestMapping(method=RequestMethod.GET)
	public List<AppSummary> appList(Account account) {
		return connectedAppRepository.findAppSummaries(account.getId());
	}

	@RequestMapping(method=RequestMethod.POST)
	public String createApp(@Valid AppForm form, BindingResult bindingResult, Account account) {
		// TODO SPR-7539 push this check back into the framework code
		if (bindingResult.hasErrors()) {
			return null;
		}
		String slug = connectedAppRepository.createApp(account.getId(), form);
		return "redirect:/develop/apps/" + slug;
	}
	
	@RequestMapping(value="/{slug}", method=RequestMethod.GET)
	public String getAppView(@PathVariable String slug, Account account, Model model) {
		App app = connectedAppRepository.findApp(account.getId(), slug);
		model.addAttribute(app);
		return "develop/apps/view";
	}
	
	@RequestMapping(value="/{slug}", method=RequestMethod.DELETE)
	public String deleteApp(@PathVariable String slug, Account account) {
		connectedAppRepository.deleteApp(account.getId(), slug);
		return "redirect:/develop/apps";
	}

	@RequestMapping(value="/{slug}", method=RequestMethod.POST)
	public String updateApp(@PathVariable String slug, @Valid AppForm form, BindingResult bindingResult, Account account) {
		if (bindingResult.hasErrors()) {
			return null;
		}
		slug = connectedAppRepository.updateApp(account.getId(), slug, form);
		return "redirect:/develop/apps/" + slug;
	}

	@RequestMapping(value="/new", method=RequestMethod.GET)
	public AppForm getNewAppForm() {
		return connectedAppRepository.getNewAppForm();
	}

	@RequestMapping(value="/edit/{slug}", method=RequestMethod.GET)
	public String getEditAppForm(@PathVariable String slug, Account account, Model model) {
		AppForm form = connectedAppRepository.getAppForm(account.getId(), slug);
		model.addAttribute(form);		
		return "develop/apps/edit";
	}
	
}
