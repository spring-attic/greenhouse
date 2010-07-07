package com.springsource.greenhouse.signin.password;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springsource.greenhouse.utils.EmailUtils;

@Controller
@RequestMapping("/password")
public class PasswordResetController {
	private final PasswordResetMessageGateway resetMessageGateway;
	private final JdbcTemplate jdbcTemplate;

	@Inject
	public PasswordResetController(JdbcTemplate jdbcTemplate, PasswordResetMessageGateway resetMessageGateway) {
		this.jdbcTemplate = jdbcTemplate;
		this.resetMessageGateway = resetMessageGateway;
	}
	
	@RequestMapping(value="/resetRequestForm", method=RequestMethod.GET)
	public void showResetRequestForm() {}
	
	@RequestMapping(value="/resetRequestForm", method=RequestMethod.POST)
	public String sendResetEmail(@RequestParam("username") String username) {
		try {
			String userQuery = EmailUtils.isEmail(username) ? "select id, email from User where email=?" : "select email from User where username=?";

			Map<String, Object> userEmailResults = jdbcTemplate.queryForMap(userQuery, username);
			BigInteger userId = (BigInteger) userEmailResults.get("id");
			String email = (String) userEmailResults.get("email");
			
			String requestKey = UUID.fromString(username).toString();
			jdbcTemplate.update("insert into PasswordResetRequest (userId, requestKey) values (?, ?)", userId, requestKey);
			
			resetMessageGateway.publish(new PasswordResetMessage(requestKey, email));
			return "password/resetInstructionsSent";
		} catch (UsernameNotFoundException e) {
			return "";
		}
	}
	
	@RequestMapping(value="/reset/{requestKey}", method=RequestMethod.GET)
	public String showResetForm(@PathVariable("requestKey") String requestKey, Map<String, String> model, HttpServletResponse response) {
			int matches = jdbcTemplate.queryForInt("select count(userId) from PasswordResetRequest where requestKey=?", requestKey);
		if(matches == 1) {
			model.put("requestKey", requestKey);
			return "password/resetForm";
		} else {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return "errors/notFound";
		} 		
	}	
}

