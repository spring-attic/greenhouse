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

/**
 * UI Controller for managing developer applications.
 * @author Keith Donald
 */
@Controller
public class AppController {

	private final AppRepository connectedAppRepository;

	@Inject
	public AppController(AppRepository connectedAppService) {
		this.connectedAppRepository = connectedAppService;
	}

	/**
	 * List all applications to the developer.
	 */
	@RequestMapping(value="/develop/apps", method=RequestMethod.GET)
	public List<AppSummary> list(Account account) {
		return connectedAppRepository.findAppSummaries(account.getId());
	}

	/**
	 * Render a blank form that allows the developer to register a new application.
	 */
	@RequestMapping(value="/develop/apps/new", method=RequestMethod.GET)
	public AppForm newForm() {
		return connectedAppRepository.getNewAppForm();
	}
	
	/**
	 * Register a new application for the developer.
	 */
	@RequestMapping(value="/develop/apps", method=RequestMethod.POST)
	public String create(@Valid AppForm form, BindingResult bindingResult, Account account) {
		if (bindingResult.hasErrors()) {
			return "develop/apps/new";
		}
		return "redirect:/develop/apps/" + connectedAppRepository.createApp(account.getId(), form);
	}

	/**
	 * Show the details of an application to the developer.
	 */
	@RequestMapping(value="/develop/apps/{slug}", method=RequestMethod.GET)
	public String view(@PathVariable String slug, Account account, Model model) {
		model.addAttribute(connectedAppRepository.findAppBySlug(account.getId(), slug));
		model.addAttribute("slug", slug);
		return "develop/apps/view";
	}
	
	/**
	 * Delete an application for the developer.
	 */
	@RequestMapping(value="/develop/apps/{slug}", method=RequestMethod.DELETE)
	public String delete(@PathVariable String slug, Account account) {
		connectedAppRepository.deleteApp(account.getId(), slug);
		return "redirect:/develop/apps";
	}

	/**
	 * Render a pre-populated form that allows the developer to edit an existing application.
	 */
	@RequestMapping(value="/develop/apps/edit/{slug}", method=RequestMethod.GET)
	public String editForm(@PathVariable String slug, Account account, Model model) {
		model.addAttribute(connectedAppRepository.getAppForm(account.getId(), slug));
		model.addAttribute("slug", slug);
		return "develop/apps/edit";
	}
	
	/**
	 * Update the details of an application for the developer.
	 */
	@RequestMapping(value="/develop/apps/{slug}", method=RequestMethod.PUT)
	public String update(@PathVariable String slug, @Valid AppForm form, BindingResult bindingResult, Account account, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("slug", slug);			
			return "develop/apps/edit";
		}
		return "redirect:/develop/apps/" + connectedAppRepository.updateApp(account.getId(), slug, form);
	}
	
}