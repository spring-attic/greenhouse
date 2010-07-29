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
    public void shouldFindAccountById() {
    	assertExpectedAccount(accountRepository.findAccount(1L));
    }
    
    @Test
    public void shouldFindAccountByEmail() throws Exception {
    	assertExpectedAccount(accountRepository.findAccount("cwalls@vmware.com"));
    }
    
    @Test
    public void shouldFindAccountByUsername() throws Exception {
    	assertExpectedAccount(accountRepository.findAccount("habuma"));
    }
    
    
    @Test(expected=UsernameNotFoundException.class)
    public void shouldThrowUsernameNotFoundExceptionForUnknownUsername() throws Exception {
    	accountRepository.findAccount("strangerdanger");
    }
        
    @Test(expected=UsernameNotFoundException.class)
    public void shouldThrowUsernameNotFoundExceptionForUnknownEmailAddress() throws Exception {
    	accountRepository.findAccount("stranger@danger.com");
    }

    @Test
    public void shouldFindConnectedAccount() throws Exception {
    	assertExpectedAccount(accountRepository.findByConnectedAccount("accesstoken", "facebook"));
    }

    @Test(expected=ConnectedAccountNotFoundException.class)
    public void shouldThrowExceptionForUnknownConnection() throws Exception {
    	accountRepository.findByConnectedAccount("badtoken", "facebook");
    }
    
    @Test
    public void shouldRemoveConnectedAccount() {
    	assertEquals(1, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount"));
    	accountRepository.removeConnectedAccount(1L, "facebook");
    	assertEquals(0, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount"));
    }
    
    @Test
    public void shouldConnectAccount() throws Exception {
    	assertEquals(1, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount"));
    	accountRepository.connectAccount(1L, "habuma", "twitter", "newToken", "secret");
    	assertEquals(2, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount"));
    	assertExpectedAccount(accountRepository.findByConnectedAccount("newToken", "twitter"));
    }


	private void assertExpectedAccount(Account account) {
	    assertEquals("Craig", account.getFirstName());
    	assertEquals("Walls", account.getLastName());
    	assertEquals("cwalls@vmware.com", account.getEmail());
    	assertEquals("habuma", account.getUsername());
    }
}
