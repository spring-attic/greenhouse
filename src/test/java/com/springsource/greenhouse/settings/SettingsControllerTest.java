/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.settings;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.DefaultAuthorizationRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class SettingsControllerTest {

	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;
	
	private SettingsController controller;

	private TokenStore tokenStore; 
	
    @Before
    public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().connectedApp().testData(getClass()).getDatabase();
		tokenStore = new JdbcTokenStore(db);
    	jdbcTemplate = new JdbcTemplate(db);
    	controller = new SettingsController(tokenStore, jdbcTemplate);
    	
    	AuthorizationRequest authorizationRequest = new DefaultAuthorizationRequest("a08318eb478a1ee31f69a55276f3af64", Arrays.asList("read", "write"));
		Authentication userAuthentication = new UsernamePasswordAuthenticationToken("1", "whateveryouwantittobe");
		tokenStore.storeAccessToken(new DefaultOAuth2AccessToken("authme"), new OAuth2Authentication(authorizationRequest, userAuthentication));
    	assertEquals(1, tokenStore.findTokensByUserName("1").size());
    }
    
    @After
    public void destroy() {
    	if (db != null) {
    		db.shutdown();
    	}
    }
    
    @Test
    public void settingsPage() {
    	ExtendedModelMap model = new ExtendedModelMap();
    	controller.settingsPage(testAccount(), model);
    	@SuppressWarnings("unchecked")
		List<Map<String, Object>> apps = (List<Map<String, Object>>) model.get("apps");
    	assertNotNull(apps);   
    	assertEquals(1, apps.size());
    	assertEquals("Greenhouse for the iPhone", apps.get(0).get("name"));
    	assertEquals("authme", apps.get(0).get("accessToken"));
    }
    
    @Test
    public void disconnectApp() {
    	assertEquals("redirect:/settings", controller.disconnectApp("authme", testAccount()));
    	assertEquals(0, tokenStore.findTokensByUserName("kdonald").size());
    }
	
    private Account testAccount() {
    	return new Account(1L, "Joe", "Schmoe", "joe@schmoe.com", "joe", "file://pic.jpg", new UriTemplate("http://localhost:8080/members/{profileKey}"));
    }
}
