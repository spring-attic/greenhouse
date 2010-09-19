package com.springsource.greenhouse.signup;

import javax.inject.Inject;

import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.facebook.FacebookUserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/signup")
public class FacebookSignupController {
	
	private final FacebookOperations facebook;

	@Inject
	public FacebookSignupController(FacebookOperations facebook) {
		this.facebook = facebook;
	}
	
	@RequestMapping(value="/fb", method = RequestMethod.GET)
	public String signupForm(Model model) {
		FacebookUserInfo userInfo = facebook.getUserInfo();
		SignupForm signupForm = new SignupForm();
		signupForm.setFirstName(userInfo.getFirstName());
		signupForm.setLastName(userInfo.getLastName());
		signupForm.setEmail(userInfo.getEmail());
		model.addAttribute(signupForm);
		return "signup";
	}
}
