package com.springsource.greenhouse.settings;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.ui.ExtendedModelMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.test.utils.GreenhouseTestDatabaseFactory;

public class SettingsControllerTest {

	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;
	
	private SettingsController controller; 
	
    @Before
    public void setup() {
    	db = GreenhouseTestDatabaseFactory.createTestDatabase(
    			new FileSystemResource("src/main/webapp/WEB-INF/database/schema-member.sql"),
    			new ClassPathResource("SettingsControllerTest.sql", getClass()));
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
