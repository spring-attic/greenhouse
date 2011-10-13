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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springsource.greenhouse.account.SignInNotFoundException;
import com.springsource.greenhouse.utils.Message;

/**
 * UI Controller for resetting user passwords.
 * This Controller may be accessed by the general public and does not require signin.
 * However, it should be accessed over SSL since it accepts secure user credentials.
 * @author Keith Donald
 */
@Controller
public class ResetPasswordController {
	
	private final ResetPasswordService service;
	
	@Inject
	public ResetPasswordController(ResetPasswordService service) {
		this.service = service;
	}
	
	/**
	 * Render the reset password form to the person as HTML in their web browser.
	 */
	@RequestMapping(value="/reset", method=RequestMethod.GET)
	public void resetPage(Model model) {
		model.addAttribute("username", new FieldModel<String>());
	}
	
	/**
	 * Process a request by a person to reset a member password.
	 * Calls the {@link ResetPasswordService} to send a reset password mail.
	 * The mail message will contain a request token that must be presented to continue the password reset operation. 
	 */
	@RequestMapping(value="/reset", method=RequestMethod.POST)
	public String sendResetMail(@RequestParam String signin, Model model, RedirectAttributes redirectAttrs) {
		try {
			service.sendResetMail(signin);
			redirectAttrs.addFlashAttribute(
					Message.info("An email has been sent to you.  Follow its instructions to reset your password."));
			return "redirect:/reset";
		} catch (SignInNotFoundException e) {
			model.addAttribute("signin", FieldModel.error("not on file", signin));
			return null;
		}
	}
	
	/**
	 * Render a change password form to the user once the reset token is validated.
	 */
	@RequestMapping(value="/reset", method=RequestMethod.GET, params="token")
	public String changePasswordForm(@RequestParam String token, Model model) {
		if (!service.isValidResetToken(token)) {
			return "reset/invalidToken";
		}
		model.addAttribute(new ChangePasswordForm());
		return "reset/changePassword";
	}	

	/**
	 * Process the change password submission and reset the user's password.
	 */
	@RequestMapping(value="/reset", method=RequestMethod.POST, params="token")
	public String changePassword(@RequestParam String token, 
			@Valid ChangePasswordForm form, BindingResult formBinding, Model model, RedirectAttributes redirectAttrs) {
		if (formBinding.hasErrors()) {
			model.addAttribute("token", token);
			return "reset/changePassword";
		}
		try {
			service.changePassword(token, form.getPassword());
			redirectAttrs.addFlashAttribute("Your password has been reset");
			return "redirect:/reset";
		} catch (InvalidResetTokenException e) {
			redirectAttrs.addFlashAttribute(Message.error("Your reset password session has expired.  Please try again."));
			return "redirect:/reset";
		}
	}
	
}