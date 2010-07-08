package com.springsource.greenhouse.reset;

import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.template.StringTemplate;
import org.springframework.mail.template.StringTemplateFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.utils.EmailUtils;

@Controller
@RequestMapping("/reset")
public class ResetPasswordController {
	private JdbcTemplate jdbcTemplate;
	private StringTemplateFactory templateFactory;
	private MailSender mailSender;

	// neeeded for cglib until AspectJ is plugged in
	public ResetPasswordController() {}
	
	@Inject
	public ResetPasswordController(JdbcTemplate jdbcTemplate, 
			StringTemplateFactory templateFactory, 
			MailSender mailSender) {
		this.jdbcTemplate = jdbcTemplate;
		this.templateFactory = templateFactory;
		this.mailSender = mailSender;
	}
	
	@RequestMapping(method=RequestMethod.GET, params="request")
	public ResetRequestForm showResetRequestForm() {
		return new ResetRequestForm();
	}
	
	@RequestMapping(method=RequestMethod.POST, params="username")
	public String sendResetEmail(@NotNull @Size(max=320) String username) {
		try {
			String userQuery = EmailUtils.isEmail(username) ? 
								"select id, email from User where email=?" : 
								"select id, email from User where username=?";
	
			Map<String, Object> userEmailResults = jdbcTemplate.queryForMap(userQuery, username);
			Long userId = (Long) userEmailResults.get("id");
			String email = (String) userEmailResults.get("email");
			
			String requestKey = UUID.randomUUID().toString();
			jdbcTemplate.update("insert into PasswordResetRequest (userId, requestKey) values (?, ?)", userId, requestKey);
			
			mailSender.send(assembleResetMailMessage(email, requestKey));
		} catch (EmptyResultDataAccessException e) {
			// do nothing?
		}

		return "redirect:/reset/requestSent";
	}
	
	@RequestMapping(value="/requestSent", method=RequestMethod.GET)
	public void instructionsSent() {}
	
	@RequestMapping(value="/{requestKey}", method=RequestMethod.GET)
	public String showResetForm(@PathVariable("requestKey") String requestKey, 
								Map<String, Object> model, 
								HttpServletResponse response) {
		int matches = jdbcTemplate.queryForInt("select count(userId) from PasswordResetRequest where requestKey=?", requestKey);
		if(matches == 1) {
			model.put("resetPasswordForm", new ResetPasswordForm());
			return "reset/form";
		} else {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return "errors/notFound";
		} 		
	}	
	
	@RequestMapping(value="/{requestKey}", method=RequestMethod.POST)
	@Transactional
	public String changePassword(@Valid ResetPasswordForm resetPasswordForm, 
			                     @PathVariable("requestKey") String requestKey) {
		long userId = jdbcTemplate.queryForLong("select userId from PasswordResetRequest where requestKey=?", requestKey);		
		jdbcTemplate.update("update User set password=? where id=?", resetPasswordForm.getPassword(), userId);
		jdbcTemplate.update("delete from PasswordResetRequest where requestKey=?", requestKey);		
		return "reset/complete";
	}
	
	// Email message assembly
	private Resource welcomeTemplate = new ClassPathResource("password-reset-mail.st", getClass());
	private SimpleMailMessage assembleResetMailMessage(String email, String requestKey) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("noreply@greenhouse.springsource.com");
		mailMessage.setTo(email);
		StringTemplate textTemplate;
		mailMessage.setSubject("Reset your Greenhouse password");
		textTemplate = templateFactory.getStringTemplate(welcomeTemplate);
		textTemplate.put("requestKey", requestKey);
		mailMessage.setText(textTemplate.render());
		return mailMessage;
	}
}

