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
package com.springsource.greenhouse.reset;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.model.FieldModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.SignInNotFoundException;

/**
 * @author Keith Donald
 */
@Controller
public class ResetPasswordController {
	
	private ResetPasswordService service;
	
	@Inject
	public ResetPasswordController(ResetPasswordService service) {
		this.service = service;
	}
	
	@RequestMapping(value="/reset", method=RequestMethod.GET)
	public void resetPage(Model model) {
		model.addAttribute("username", new FieldModel<String>());
	}
	
	@RequestMapping(value="/reset", method=RequestMethod.POST)
	public String sendResetMail(@RequestParam String username, Model model) {
		try {
			service.sendResetMail(username);
			FlashMap.setInfoMessage("An email has been sent to you.  Follow its instructions to reset your password.");
			return "redirect:/reset";
		} catch (SignInNotFoundException e) {
			model.addAttribute("username", FieldModel.error("not on file", username));
			return null;
		}
	}
	
	@RequestMapping(value="/reset", method=RequestMethod.GET, params="token")
	public String changePasswordForm(@RequestParam String token, Model model) {
		if (!service.isValidResetToken(token)) {
			return "reset/invalidToken";
		}
		model.addAttribute(new ChangePasswordForm());
		return "reset/changePassword";
	}	
	
	@RequestMapping(value="/reset", method=RequestMethod.POST, params="token")
	public String changePassword(@RequestParam String token, @Valid ChangePasswordForm form, BindingResult formBinding, Model model) {
		if (formBinding.hasErrors()) {
			model.addAttribute("token", token);
			return "reset/changePassword";
		}
		try {
			service.changePassword(token, form.getPassword());
			FlashMap.setSuccessMessage("Your password has been reset");
			return "redirect:/reset";
		} catch (InvalidResetTokenException e) {
			FlashMap.setErrorMessage("Your reset password session has expired.  Please try again.");
			return "redirect:/reset";
		}
	}
	
}