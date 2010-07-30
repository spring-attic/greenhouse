package com.springsource.greenhouse.account;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.test.utils.GreenhouseTestDatabaseFactory;

public class JdbcAccountRepositoryTest {
	private EmbeddedDatabase db;
	
	private JdbcAccountRepository accountRepository;

	private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
    	db = GreenhouseTestDatabaseFactory.createTestDatabase(
    			new FileSystemResource("src/main/webapp/WEB-INF/database/schema-member.sql"),
    			new ClassPathResource("JdbcAccountRepositoryTest.sql", getClass()));
    	jdbcTemplate = new JdbcTemplate(db);
    	accountRepository = new JdbcAccountRepository(jdbcTemplate);
    }
    
    @After
    public void destroy() {
    	db.shutdown();
    }
    
    @Test
    public void findAccountById() {
    	assertExpectedAccount(accountRepository.findById(1L));
    }
    
    @Test 
    public void findAccountByEmail() throws Exception {
    	assertExpectedAccount(accountRepository.findByUsername("cwalls@vmware.com"));
    }
    
    @Test
    public void findAccountByUsername() throws Exception {
    	assertExpectedAccount(accountRepository.findByUsername("habuma"));
    }
    
    
    @Test(expected=UsernameNotFoundException.class)
    public void throwUsernameNotFoundExceptionForUnknownUsername() throws Exception {
    	accountRepository.findByUsername("strangerdanger");
    }
        
    @Test(expected=UsernameNotFoundException.class)
    public void throwUsernameNotFoundExceptionForUnknownEmailAddress() throws Exception {
    	accountRepository.findByUsername("stranger@danger.com");
    }

    @Test
    public void findConnectedAccount() throws Exception {
    	assertExpectedAccount(accountRepository.findByConnectedAccount("facebook", "accesstoken"));
    }

    @Test(expected=ConnectedAccountNotFoundException.class)
    public void throwExceptionForUnknownConnection() throws Exception {
    	accountRepository.findByConnectedAccount("badtoken", "facebook");
    }
    
    @Test
    public void disconnectAccount() {
    	assertEquals(1, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount"));
    	accountRepository.disconnect(1L, "facebook");
    	assertEquals(0, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount"));
    }
    
    @Test
    public void connectAccount() throws Exception {
    	assertEquals(1, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount"));
    	accountRepository.connect(1L, "twitter", "newToken");
    	assertEquals(2, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount"));
    	assertExpectedAccount(accountRepository.findByConnectedAccount("twitter", "newToken"));
    }

    @Test
    public void isConnected() {
    	assertTrue(accountRepository.isConnected(1L, "facebook"));
    }

    @Test
    public void notConnected() {
    	assertFalse(accountRepository.isConnected(1L, "twitter"));
    }

	private void assertExpectedAccount(Account account) {
	    assertEquals("Craig", account.getFirstName());
    	assertEquals("Walls", account.getLastName());
    	assertEquals("cwalls@vmware.com", account.getEmail());
    	assertEquals("habuma", account.getUsername());
    }
}
