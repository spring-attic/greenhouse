package com.springsource.greenhouse.settings;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import com.springsource.greenhouse.account.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/settings")
public class SettingsController {

	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public SettingsController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@RequestMapping(method=RequestMethod.GET)
	public void settingsPage(Account account, Model model) {
		List<Map<String, Object>> apps = jdbcTemplate.queryForList("select c.name as name, ac.accessToken from ConnectedApp ac, App c where ac.member = ? and ac.app = c.consumerKey", account.getId());
		model.addAttribute("apps", apps);
	}
	
	@RequestMapping(value="/apps/{accessToken}", method=RequestMethod.DELETE)
	public String disconnectApp(@PathVariable String accessToken, Account account) {
		jdbcTemplate.update("delete from ConnectedApp where accessToken = ? and member = ?", accessToken, account.getId());
		return "redirect:/settings";
	}
}
