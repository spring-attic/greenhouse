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

import com.springsource.greenhouse.account.UsernameNotFoundException;

@Controller
@RequestMapping("/reset")
public class ResetPasswordController {
	
	private ResetPasswordService service;
	
	@Inject
	public ResetPasswordController(ResetPasswordService service) {
		this.service = service;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public void resetPage(Model model) {
		model.addAttribute("username", new FieldModel<String>());
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String sendResetMail(@RequestParam String username, Model model) {
		try {
			service.sendResetMail(username);
			FlashMap.setInfoMessage("An email has been sent to you.  Follow its instructions to reset your password.");
			return "redirect:/reset";
		} catch (UsernameNotFoundException e) {
			model.addAttribute("username", FieldModel.error("not a valid username", username));
			return null;
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, params="token")
	public String changePasswordForm(@RequestParam String token, Model model) {
		if (!service.isValidResetToken(token)) {
			return "reset/invalidToken";
		}
		model.addAttribute(new ChangePasswordForm());
		return "reset/changePassword";
	}	
	
	@RequestMapping(method=RequestMethod.POST, params="token")
	public String changePassword(@RequestParam String token, @Valid ChangePasswordForm form, BindingResult formBinding, Model model) {
		if (formBinding.hasErrors()) {
			model.addAttribute("token", token);
			return "reset/changePassword";
		}
		try {
			service.changePassword(token, form.getPassword());
			FlashMap.setSuccessMessage("Your password has been reset successfully");
			return "redirect:/reset";
		} catch (InvalidResetTokenException e) {
			FlashMap.setErrorMessage("Your reset password session has expired.  Please try again.");
			return "redirect:/reset";
		}
	}
	
}