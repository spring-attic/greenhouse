package com.springsource.greenhouse.members;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.test.utils.GreenhouseTestDatabaseFactory;

public class JdbcMemberRepositoryTest {
	private EmbeddedDatabase db;
	
	private JdbcMemberRepository memberRepository;

	private JdbcTemplate jdbcTemplate;

	@Before
	public void setup() {
    	db = GreenhouseTestDatabaseFactory.createTestDatabase(
    			new FileSystemResource("src/main/webapp/WEB-INF/database/schema-member.sql"),
    			new ClassPathResource("JdbcMemberRepositoryTest.sql", getClass()));
    	jdbcTemplate = new JdbcTemplate(db);
    	memberRepository = new JdbcMemberRepository(jdbcTemplate);
    }
	
    @After
    public void destroy() {
    	db.shutdown();
    }

	@Test
	public void shouldFindMemberById() {
		assertExpectedMember(memberRepository.findMemberByAccountId(1L));
	}

	@Test
	public void shouldFindMemberByUsername() {
		assertExpectedMember(memberRepository.findMemberByUsername("habuma"));
	}

	@Test
	public void shouldFindMemberByProfileKey() {
		assertExpectedMember(memberRepository.findMemberByProfileKey("habuma"));
		assertExpectedMember(memberRepository.findMemberByProfileKey("1"));
	}
	
	@Test
	public void shouldLookupConnectedIdsByUsername() {
		Map<String, String> connectedIds = memberRepository.lookupConnectedAccountIds("habuma");
		assertExpectedConnectedIds(connectedIds);
	}

	@Test
	public void shouldLookupConnectedIdsById() {
		Map<String, String> connectedIds = memberRepository.lookupConnectedAccountIds("1");
		assertExpectedConnectedIds(connectedIds);
	}
	
	@Test
	public void shouldReturnEmptyMapForUserWithNoConnections() {
		Map<String, String> connectedIds = memberRepository.lookupConnectedAccountIds("2");
		assertEquals(0, connectedIds.size());
	}

	private void assertExpectedMember(Member member) {
	    assertEquals("Craig", member.getFirstName());
		assertEquals("Walls", member.getLastName());
    }

	private void assertExpectedConnectedIds(Map<String, String> connectedIds) {
	    assertEquals(2, connectedIds.size());
		assertEquals("habuma", connectedIds.get("twitter"));
		assertEquals("123456789", connectedIds.get("facebook"));
    }	
}
