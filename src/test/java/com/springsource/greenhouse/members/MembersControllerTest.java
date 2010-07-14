package com.springsource.greenhouse.members;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.ui.ExtendedModelMap;

import com.springsource.greenhouse.test.utils.GreenhouseTestDatabaseFactory;

public class MembersControllerTest {

	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;
	
	private MembersController controller; 
	
    @Before
    public void setup() {
    	db = GreenhouseTestDatabaseFactory.createTestDatabase(
    			new FileSystemResource("src/main/webapp/WEB-INF/database/schema-member.sql"),
    			new ClassPathResource("MembersControllerTest.sql", getClass()));
    	jdbcTemplate = new JdbcTemplate(db);
    	MembersService membersService = new DefaultMembersService(jdbcTemplate);
    	controller = new MembersController(membersService);
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
