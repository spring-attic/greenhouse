package com.springsource.greenhouse.signup;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.ProfilePictureException;
import com.springsource.greenhouse.account.ProfilePictureService;
import com.springsource.greenhouse.utils.SecurityUtils;

@Controller
@RequestMapping("/signup")
public class SignupController {

	private SignupService signupService;
	private final ProfilePictureService profilePictureService;
		
	@Inject
	public SignupController(SignupService signupService, ProfilePictureService profilePictureService) {
		this.signupService = signupService;
		this.profilePictureService = profilePictureService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public SignupForm signupForm() {
		return new SignupForm();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding, 
			@RequestParam(value="profileImage", required=false) MultipartFile profileImage, HttpServletRequest request) {
		if (formBinding.hasErrors()) {
			return null;
		}
		try {
			Account account = signupService.signup(form.createPerson());			
			SecurityUtils.signin(account);
			profilePictureService.setProfilePicture(account.getId(), profileImage.getBytes(), profileImage.getContentType());
		} catch (EmailAlreadyOnFileException e) {
			formBinding.rejectValue("email", "account.duplicateEmail", "already on file");
			return null;
		} catch (ProfilePictureException e) {
			// TODO: Figure out best way to handle this
		} catch (IOException e) {
			// TODO: Figure out best way to handle this
		}
		return "redirect:/";			
	}
}