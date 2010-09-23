package com.springsource.greenhouse.invite;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountMapper;
import com.springsource.greenhouse.account.PictureSize;
import com.springsource.greenhouse.account.PictureUrlFactory;
import com.springsource.greenhouse.account.PictureUrlMapper;
import com.springsource.greenhouse.account.StubFileStorage;
import com.springsource.greenhouse.connect.JdbcAccountProviderRepository;
import com.springsource.greenhouse.connect.TwitterAccountProvider;
import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class TwitterInviteControllerTest {
	
	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;

	private TwitterInviteController controller;

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().connectedAccount().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		AccountMapper accountMapper = new AccountMapper(new PictureUrlMapper(new PictureUrlFactory(new StubFileStorage()), PictureSize.small), new UriTemplate("http://localhost:8080/members/{profileKey}"));	
		JdbcAccountProviderRepository providerRepository = new JdbcAccountProviderRepository(jdbcTemplate, accountMapper);
		TwitterAccountProvider twitterProvider = (TwitterAccountProvider) providerRepository.findAccountProviderByName("twitter");
		controller = new TwitterInviteController(twitterProvider);
	}

	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}

	@Test
	public void friendFinder() {
		Account account = new Account(1L, "Joe", "Schmoe", "joe@schmoe.com", "joe", "file://pic.jpg", new UriTemplate("http://localhost:8080/members/{id}"));
		Model model = new ExtendedModelMap();
		controller.friendFinder(account, model);
		assertEquals("habuma", model.asMap().get("username"));
	}

	@Test
	public void friendFinderNotConnected() {
		Account account = new Account(2L, "Sue", "Schmoe", "sue@schmoe.com", "sue", "file://pic.jpg", new UriTemplate("http://localhost:8080/members/{id}"));
		Model model = new ExtendedModelMap();
		controller.friendFinder(account, model);
		assertFalse(model.containsAttribute("username"));
	}

}