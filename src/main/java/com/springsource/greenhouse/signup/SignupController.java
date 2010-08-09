package com.springsource.greenhouse.signup;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.s3.S3Operations;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.SecurityUtils;

@Controller
@RequestMapping("/signup")
public class SignupController {

	private SignupService signupService;
	private final S3Operations s3;
	private final JdbcTemplate jdbcTemplate;
		
	@Inject
	public SignupController(SignupService signupService, S3Operations s3, JdbcTemplate jdbcTemplate) {
		this.signupService = signupService;
		this.s3 = s3;
		this.jdbcTemplate = jdbcTemplate;
	}

	@RequestMapping(method = RequestMethod.GET)
	public SignupForm signupForm() {
		return new SignupForm();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding, 
			@RequestParam(value="profileImage", required=false) MultipartFile profileImage, HttpServletRequest request) throws Exception {
		if (formBinding.hasErrors()) {
			return null;
		}
		try {
			Account account = signupService.signup(form.createPerson());			
			if(!profileImage.isEmpty()) {
				// TODO: Figure out real extension from mime type or original file name
				String imageUrl = s3.saveFile("gh-images", "profilepix/" + account.getId() + ".jpg", 
						profileImage.getBytes(), profileImage.getContentType());
								
				jdbcTemplate.update("update member set imageUrl = ? where id = ?", imageUrl, account.getId());
			}
			SecurityUtils.signin(account);
		} catch (EmailAlreadyOnFileException e) {
			formBinding.rejectValue("email", "account.duplicateEmail", "already on file");
			return null;
		}
		return "redirect:/";			
	}
}