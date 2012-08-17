/*
 * Copyright 2012 the original author or authors.
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
package com.springsource.greenhouse.develop.oauth;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springsource.greenhouse.develop.AppRepository;
import com.springsource.greenhouse.develop.InvalidApiKeyException;

/**
 * The UI Controller that shows the application authorization form to the member.
 * @author Keith Donald
 * @author Craig Walls
 */
@Controller
public class ConfirmAccessController {
	
	private final AppRepository appRepository;

	@Inject
	public ConfirmAccessController(AppRepository appRepository) {
		this.appRepository = appRepository;
	}

	@RequestMapping(value="/oauth/confirm_access", method=RequestMethod.GET)
	protected String confirmAccessForm(@RequestParam("client_id") String apiKey, Model model) throws InvalidRequestTokenException, InvalidApiKeyException  {
		model.addAttribute("clientApp", appRepository.findAppByApiKey(apiKey));
		return "oauth/confirmAccess";
	}

}