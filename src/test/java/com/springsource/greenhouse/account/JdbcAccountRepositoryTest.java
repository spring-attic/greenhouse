package com.springsource.greenhouse.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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
    public void create() throws EmailAlreadyOnFileException {
    	// TODO support @Transactional without requiring container XML
    	PlatformTransactionManager tm = new DataSourceTransactionManager(db);
    	TransactionStatus txStatus = tm.getTransaction(new DefaultTransactionDefinition());
    	
    	Person person = new Person("Jack", "Black", "jack@black.com", "foobie", Gender.Male, new LocalDate(1977, 12, 1));
    	Account account = accountRepository.createAccount(person);
    	assertEquals(3L, (long) account.getId());
    	assertEquals("Jack Black", account.getFullName());
    	assertEquals("jack@black.com", account.getEmail());
    	assertEquals("3", account.getProfileKey());
    	assertEquals("http://images.greenhouse.springsource.org/profile-pics/male/small.jpg", account.getProfilePictureUrl());
    	
    	tm.rollback(txStatus);
    }

    @Test
    public void findById() {
    	assertExpectedAccount(accountRepository.findById(1L));
    }
    
    @Test 
    public void findtByEmail() throws Exception {
    	assertExpectedAccount(accountRepository.findByUsername("cwalls@vmware.com"));
    }
    
    @Test
    public void findByUsername() throws Exception {
    	assertExpectedAccount(accountRepository.findByUsername("habuma"));
    }
    
    @Test(expected=UsernameNotFoundException.class)
    public void usernameNotFound() throws Exception {
    	accountRepository.findByUsername("strangerdanger");
    }
        
    @Test(expected=UsernameNotFoundException.class)
    public void usernameNotFoundEmail() throws Exception {
    	accountRepository.findByUsername("stranger@danger.com");
    }

    @Test
    public void findConnectedAccount() throws Exception {
    	assertExpectedAccount(accountRepository.findByConnectedAccount("facebook", "accesstoken"));
    }

    @Test(expected=ConnectedAccountNotFoundException.class)
    public void connectedAccountNotFound() throws Exception {
    	accountRepository.findByConnectedAccount("badtoken", "facebook");
    }
    
    @Test
    public void findFriendAccounts() throws Exception {
    	List<Account> accounts = accountRepository.findFriendAccounts("facebook", Collections.singletonList("1"));
    	assertEquals(1, accounts.size());
    	assertExpectedAccount(accounts.get(0));
    }
    
    @Test
    public void disconnectAccount() {
    	assertEquals(1, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount where member = 1 and provider = 'facebook'"));
    	accountRepository.disconnect(1L, "facebook");
    	assertEquals(0, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount where member = 1 and provider = 'facebook'"));
    }
    
    @Test
    public void connectAccount() throws Exception {
    	assertEquals(0, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount where member = 1 and provider = 'tripit'"));
    	accountRepository.connect(1L, "tripit", "accessToken", "cwalls");
    	assertEquals(1, jdbcTemplate.queryForInt("select count(*) from ConnectedAccount where member = 1 and provider = 'tripit'"));
    	assertExpectedAccount(accountRepository.findByConnectedAccount("tripit", "accessToken"));
    }

    @Test(expected=AccountAlreadyConnectedException.class)
    public void accountAlreadyConnected() throws Exception {
    	accountRepository.connect(1L, "facebook", "accessToken", "cwalls");
    }

    
    @Test
    public void isConnected() {
    	assertTrue(accountRepository.isConnected(1L, "facebook"));
    }

    @Test
    public void notConnected() {
    	assertFalse(accountRepository.isConnected(1L, "tripit"));
    }
    
    @Test
    public void markProfilePictureSet() {
    	accountRepository.markProfilePictureSet(1L);
    	assertTrue(jdbcTemplate.queryForObject("select pictureSet from Member where id = ?", Boolean.class, 1L));
    }

	private void assertExpectedAccount(Account account) {
	    assertEquals("Craig", account.getFirstName());
    	assertEquals("Walls", account.getLastName());
    	assertEquals("Craig Walls", account.getFullName());
    	assertEquals("cwalls@vmware.com", account.getEmail());
    	assertEquals("habuma", account.getUsername());
    	assertEquals("habuma", account.getProfileKey());
    	assertEquals("http://images.greenhouse.springsource.org/profile-pics/male/small.jpg", account.getProfilePictureUrl());
    }
}
