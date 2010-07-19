package com.springsource.greenhouse.events;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.test.utils.GreenhouseTestDatabaseFactory;

public class EventsServiceTest {
	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;
	
	private EventsService service; 
	
    @Before
    public void setup() {
    	db = GreenhouseTestDatabaseFactory.createTestDatabase(
    			new FileSystemResource("src/main/webapp/WEB-INF/database/schema-member.sql"),
    			new FileSystemResource("src/main/webapp/WEB-INF/database/schema-event.sql"),
    			new ClassPathResource("EventsServiceTest.sql", getClass()));
    	jdbcTemplate = new JdbcTemplate(db);
    	service = new DefaultEventsService(jdbcTemplate);
    }
    
    @After
    public void destroy() {
    	db.shutdown();
    }
    
    @Test
    public void shouldGetEventsAfterAGivenDate() {
    	Calendar testDate = Calendar.getInstance();
    	testDate.set(2010, 7, 14);
		List<Event> events = service.findEventsAfter(testDate.getTime());
    	assertEquals(2, events.size());    	
    	assertEquals(2, events.get(0).getId());
    	assertEquals(1, events.get(1).getId());
    }
    
    @Test
    public void shouldGetEventById() {
    	Event event = service.findEventById(2);
    	assertExpectedSoonEvent(event);
    }
    
    @Test
    public void shouldFindAnEventByEventName() {
    	Event event = service.findEventByPublicId("Soon_Event");
    	assertExpectedSoonEvent(event);
    }
    
	private void assertExpectedSoonEvent(Event event) {
	    assertEquals(2, event.getId());
	    assertEquals("Soon_Event", event.getFriendlyId());
    	assertEquals("Soon Event", event.getTitle());
    	assertEquals("This event is soon", event.getDescription());
    	assertEquals("#soon", event.getHashtag());
    	assertEquals("Chicago, IL", event.getLocation());
    	assertEquals(1287464400000L, event.getStartTime().getTime());
    	assertEquals(1287723600000L, event.getEndTime().getTime());
    }
}
