package com.springsource.greenhouse.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.ui.ExtendedModelMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.settings.SettingsController;
import com.springsource.greenhouse.signup.GreenhouseTestUserDatabaseFactory;

public class SettingsControllerTest {

	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;
	
	private SettingsController controller; 
	
    @Before
    public void setup() {
    	db = GreenhouseTestUserDatabaseFactory.createUserDatabase(new ClassPathResource("SettingsControllerTest.sql", getClass()));
    	jdbcTemplate = new JdbcTemplate(db);
    	controller = new SettingsController(jdbcTemplate);
    }
    
    @After
    public void destroy() {
    	db.shutdown();
    }

    @Test
    public void testPrepareSettingsPage() {
    	ExtendedModelMap model = new ExtendedModelMap();
    	controller.settingsPage(new Account(1L), model);
    	List<Map<String, Object>> apps = (List<Map<String, Object>>) model.get("apps");
    	assertNotNull(apps);   
    	assertEquals(1, apps.size());
    	assertEquals("Greenhouse for the iPhone", apps.get(0).get("name"));
    	assertEquals("authme", apps.get(0).get("accessToken"));
    }
    
    @Test
    public void testDisconnectApp() {
    	assertEquals("redirect:/settings", controller.disconnectApp("authme", new Account(1L)));
    	assertEquals(0, jdbcTemplate.queryForInt("select count(*) from ConnectedApp"));
    }
	
}
