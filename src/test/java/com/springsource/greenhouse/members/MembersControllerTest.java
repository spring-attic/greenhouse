package com.springsource.greenhouse.members;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.ui.ExtendedModelMap;

import com.springsource.greenhouse.signup.GreenhouseTestUserDatabaseFactory;

public class MembersControllerTest {

	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;
	
	private MembersController controller; 
	
    @Before
    public void setup() {
    	db = GreenhouseTestUserDatabaseFactory.createUserDatabase(new ClassPathResource("MembersControllerTest.sql", getClass()));
    	jdbcTemplate = new JdbcTemplate(db);
    	controller = new MembersController(jdbcTemplate);
    }
    
    @After
    public void destroy() {
    	db.shutdown();
    }

    @Test
    public void testMemberViewForUsername() {
    	ExtendedModelMap model = new ExtendedModelMap();
    	controller.memberView("kdonald", model);
    	Member member = (Member) model.get("member");
    	assertEquals("Keith", member.getFirstName());
    	assertEquals("Donald", member.getLastName());
    }

    @Test
    public void testMemberViewForId() {
    	ExtendedModelMap model = new ExtendedModelMap();
    	controller.memberView("1", model);
    	Member member = (Member) model.get("member");
    	assertEquals("Keith", member.getFirstName());
    	assertEquals("Donald", member.getLastName());
    }

}
