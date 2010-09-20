package com.springsource.greenhouse.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import com.springsource.greenhouse.account.Account;

import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;
import com.springsource.greenhouse.settings.SettingsController;

public class SettingsControllerTest {

	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;
	
	private SettingsController controller; 
	
    @Before
    public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().connectedApp().testData(getClass()).getDatabase();
    	jdbcTemplate = new JdbcTemplate(db);
    	controller = new SettingsController(jdbcTemplate);
    }
    
    @After
    public void destroy() {
    	if (db != null) {
    		db.shutdown();
    	}
    }

    @Test
    public void settingsPage() {
    	ExtendedModelMap model = new ExtendedModelMap();
    	controller.settingsPage(testAccount(), model);
    	List<Map<String, Object>> apps = (List<Map<String, Object>>) model.get("apps");
    	assertNotNull(apps);   
    	assertEquals(1, apps.size());
    	assertEquals("Greenhouse for the iPhone", apps.get(0).get("name"));
    	assertEquals("authme", apps.get(0).get("accessToken"));
    }
    
    @Test
    public void disconnectApp() {
    	assertEquals("redirect:/settings", controller.disconnectApp("authme", testAccount()));
    	assertEquals(0, jdbcTemplate.queryForInt("select count(*) from AppConnection"));
    }
	
    private Account testAccount() {
    	return new Account(1L, "Joe", "Schmoe", "joe@schmoe.com", "joe", "file://pic.jpg", new UriTemplate("http://localhost:8080/members/{profileKey}"));
    }
}
