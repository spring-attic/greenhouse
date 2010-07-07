package com.springsource.greenhouse.signin.password;

import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.utils.EmailUtils;

@Controller
@RequestMapping("/password")
public class ResetPasswordController {
	private ResetPasswordMessageGateway resetMessageGateway;
	private JdbcTemplate jdbcTemplate;

	// neeeded for cglib until AspectJ is plugged in
	public ResetPasswordController() {}
	
	@Inject
	public ResetPasswordController(JdbcTemplate jdbcTemplate, ResetPasswordMessageGateway resetMessageGateway) {
		this.jdbcTemplate = jdbcTemplate;
		this.resetMessageGateway = resetMessageGateway;
	}
	
	@RequestMapping(value="/resetRequest", method=RequestMethod.GET)
	public ResetPasswordRequestForm showResetRequestForm() {
		return new ResetPasswordRequestForm();
	}
	
	@RequestMapping(value="/resetRequest", method=RequestMethod.POST)
	public String sendResetEmail(@Valid ResetPasswordRequestForm resetRequestForm) {
		try {
			String userQuery = EmailUtils.isEmail(resetRequestForm.getUsername()) ? 
								"select id, email from User where email=?" : 
								"select id, email from User where username=?";

			Map<String, Object> userEmailResults = jdbcTemplate.queryForMap(userQuery, resetRequestForm.getUsername());
			Long userId = (Long) userEmailResults.get("id");
			String email = (String) userEmailResults.get("email");
			
			String requestKey = UUID.randomUUID().toString();
			jdbcTemplate.update("insert into PasswordResetRequest (userId, requestKey) values (?, ?)", userId, requestKey);
			
			resetMessageGateway.publish(new ResetPasswordMessage(requestKey, email));
			return "password/resetInstructionsSent";
		} catch (UsernameNotFoundException e) {
			return ""; // TODO - Handle this case correctly
		}
	}
	
	@RequestMapping(value="/reset/{requestKey}", method=RequestMethod.GET)
	public String showResetForm(@PathVariable("requestKey") String requestKey, 
								Map<String, Object> model, 
								HttpServletResponse response) {
		int matches = jdbcTemplate.queryForInt("select count(userId) from PasswordResetRequest where requestKey=?", requestKey);
		if(matches == 1) {
			model.put("resetPasswordForm", new ResetPasswordForm());
			return "password/resetForm";
		} else {
			// TODO : Is there a better way of handling this?
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return "errors/notFound";
		} 		
	}	
	
	@RequestMapping(value="/reset/{requestKey}", method=RequestMethod.POST)
	@Transactional
	public String changePassword(@Valid ResetPasswordForm resetPasswordForm, 
			                     @PathVariable("requestKey") String requestKey) {
		long userId = jdbcTemplate.queryForLong("select userId from PasswordResetRequest where requestKey=?", requestKey);		
		jdbcTemplate.update("update User set password=? where id=?", resetPasswordForm.getPassword(), userId);
		jdbcTemplate.update("delete from PasswordResetRequest where requestKey=?", requestKey);		
		return "password/resetComplete";
	}
}

