package com.springsource.greenhouse.develop;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.security.encrypt.StandardStringEncryptor;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcAppRepositoryTest {
	
	private EmbeddedDatabase db;
	
	private JdbcAppRepository appRepository;

	private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
    	db = new GreenhouseTestDatabaseBuilder().member().connectedApp().testData(getClass()).getDatabase();
    	jdbcTemplate = new JdbcTemplate(db);
    	appRepository = new JdbcAppRepository(jdbcTemplate, new StandardStringEncryptor("PBEWithMD5AndDES", "secret"));
    }
    
	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}

    @Test
    public void findAppSummaries() {
    	List<AppSummary> summaries = appRepository.findAppSummaries(2L);
    	assertEquals(1, summaries.size());
    	AppSummary summary = summaries.get(0);
    	assertEquals("Greenhouse for Facebook", summary.getName());
    	assertEquals("Awesome", summary.getDescription());
    	assertEquals("http://images.greenhouse.springsource.org/default-app-icon.jpg", summary.getIconUrl());
    	assertEquals("greenhouse-for-facebook", summary.getSlug());
    	
    	summaries = appRepository.findAppSummaries(3L);
    	assertEquals(2, summaries.size());
    	summary = summaries.get(0);
    	assertEquals("Greenhouse for the iPhone", summary.getName());    	
    	summary = summaries.get(1);
    	assertEquals("Greenhouse for the Android", summary.getName());    	
    }
    
    @Test
    public void noAppSummaries() {
    	assertEquals(0, appRepository.findAppSummaries(1L).size());
    }
      
    @Test
    public void createApp() {
    	AppForm form = new AppForm();
    	form.setName("My App");
    	form.setDescription("My App Description");
    	String slug = appRepository.createApp(1L, form);
    	assertEquals("my-app", slug);
    	
    	App app = appRepository.findApp(1L, slug);
    	assertEquals("My App", app.getSummary().getName());
    	assertEquals(null, app.getCallbackUrl());
    }

}

