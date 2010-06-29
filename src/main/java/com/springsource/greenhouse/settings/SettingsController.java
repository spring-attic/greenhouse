package com.springsource.greenhouse.settings;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.signin.GreenhouseUserDetails;

@Controller
@RequestMapping("/settings")
public class SettingsController {

	private JdbcTemplate jdbcTemplate;
	
	public SettingsController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@RequestMapping(method=RequestMethod.GET)
	public void settingsPage(GreenhouseUserDetails currentUser, Model model) {
		List<Map<String, Object>> apps = jdbcTemplate.queryForList("select c.name as appName, ac.accessToken from AuthorizedConsumer ac, Consumer c where ac.userId = ? and ac.consumerKey = c.consumerKey", currentUser.getEntityId());
		model.addAttribute("apps", apps);
	}
	
	@RequestMapping(value="/apps/{accessToken}", method=RequestMethod.DELETE)
	public String disconnectApp(@PathVariable String accessToken, GreenhouseUserDetails currentUser) {
		jdbcTemplate.update("delete from AuthorizedConsumer where accessToken = ? and userId = ?", accessToken, currentUser.getEntityId());
		return "settings";
	}
}
