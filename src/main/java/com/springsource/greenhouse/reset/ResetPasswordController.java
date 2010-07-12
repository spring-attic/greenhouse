package com.springsource.greenhouse.reset;

import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.template.StringTemplate;
import org.springframework.mail.template.StringTemplateFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.utils.EmailUtils;

@Controller
@RequestMapping("/reset")
public class ResetPasswordController {
	
	private JdbcTemplate jdbcTemplate;
	
	private StringTemplateFactory templateFactory;

	private MailSender mailSender;

	@Inject
	public ResetPasswordController(JdbcTemplate jdbcTemplate, StringTemplateFactory templateFactory, MailSender mailSender) {
		this.jdbcTemplate = jdbcTemplate;
		this.templateFactory = templateFactory;
		this.mailSender = mailSender;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public void resetPage() {		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String sendResetMail(@RequestParam String username, Model model, HttpServletRequest request) {
		Map<String, Object> member = findByUsername(username);
		if (member == null) {
			// TODO a dynamically generated PresentationModel would be nice here
			model.addAttribute("username", username);
			model.addAttribute("error", true);
			model.addAttribute("errorMessage", "not a valid username");
			return null;
		}
		String token = UUID.randomUUID().toString();
		jdbcTemplate.update("insert into ResetPassword (token, member) values (?, ?)", token, member.get("id"));
		// TODO make this call in a separate thread
		mailSender.send(createResetMailMessage((String) member.get("email"), token));	
		FlashMap.getCurrent(request).put("successMessage", "Email sent");
		return "redirect:/reset";
	}
	
	@RequestMapping(method=RequestMethod.GET, params="token")
	public String changePasswordForm(@RequestParam String token, Model model) {
		boolean tokenPresent = jdbcTemplate.queryForInt("select count(*) from ResetPassword where token = ?", token) == 1;
		if (tokenPresent) {
			model.addAttribute(new ChangePasswordForm());
			model.addAttribute("token", token);
			return "reset/changePassword";
		} else {
			return "reset/invalidToken";
		}
	}	
	
	@RequestMapping(method=RequestMethod.POST, params="token")
	@Transactional
	public String changePassword(@RequestParam String token, @Valid ChangePasswordForm form, BindingResult bindingResult, Model model, HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("token", token);
			return "reset/changePassword";
		}
		Long memberId = jdbcTemplate.queryForLong("select member from ResetPassword where token = ?", token);		
		jdbcTemplate.update("update Member set password = ? where id = ?", form.getPassword(), memberId);
		jdbcTemplate.update("delete from ResetPassword where token = ?", token);
		FlashMap.getCurrent(request).put("successMessage", "Password reset");		
		return "redirect:/reset";
	}
	
	// internal helpers
	
	private Map<String, Object> findByUsername(String username) {
		String userQuery = EmailUtils.isEmail(username) ? 
				"select id, email from Member where email = ?" : 
				"select id, email from Member where username = ?";
		try {
			return jdbcTemplate.queryForMap(userQuery, username);		
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	private SimpleMailMessage createResetMailMessage(String email, String token) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("noreply@greenhouse.springsource.com");
		mailMessage.setTo(email);
		StringTemplate textTemplate;
		mailMessage.setSubject("Reset your Greenhouse password");
		textTemplate = templateFactory.getStringTemplate(new ClassPathResource("reset-mail.st", getClass()));
		textTemplate.put("token", token);
		mailMessage.setText(textTemplate.render());
		return mailMessage;
	}

}