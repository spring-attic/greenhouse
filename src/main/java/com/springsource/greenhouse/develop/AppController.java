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
	public List<AppSummary> list(Account account) {
		return connectedAppRepository.findAppSummaries(account.getId());
	}

	@RequestMapping(method=RequestMethod.POST)
	public String create(@Valid AppForm form, BindingResult bindingResult, Account account) {
		if (bindingResult.hasErrors()) {
			return "develop/apps/new";
		}
		return "redirect:/develop/apps/" + connectedAppRepository.createApp(account.getId(), form);
	}
	
	@RequestMapping(value="/{slug}", method=RequestMethod.GET)
	public String view(@PathVariable String slug, Account account, Model model) {
		model.addAttribute(connectedAppRepository.findAppBySlug(account.getId(), slug));
		model.addAttribute("slug", slug);
		return "develop/apps/view";
	}
	
	@RequestMapping(value="/{slug}", method=RequestMethod.DELETE)
	public String delete(@PathVariable String slug, Account account) {
		connectedAppRepository.deleteApp(account.getId(), slug);
		return "redirect:/develop/apps";
	}

	@RequestMapping(value="/{slug}", method=RequestMethod.PUT)
	public String update(@PathVariable String slug, @Valid AppForm form, BindingResult bindingResult, Account account, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("slug", slug);			
			return "develop/apps/edit";
		}
		return "redirect:/develop/apps/" + connectedAppRepository.updateApp(account.getId(), slug, form);
	}

	@RequestMapping(value="/new", method=RequestMethod.GET)
	public AppForm newForm() {
		return connectedAppRepository.getNewAppForm();
	}

	@RequestMapping(value="/edit/{slug}", method=RequestMethod.GET)
	public String editForm(@PathVariable String slug, Account account, Model model) {
		model.addAttribute(connectedAppRepository.getAppForm(account.getId(), slug));
		model.addAttribute("slug", slug);
		return "develop/apps/edit";
	}
	
}