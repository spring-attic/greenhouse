package com.springsource.greenhouse.oauth;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;

import com.springsource.greenhouse.signin.GreenhouseUserDetails;

public class NetworkConnectionsTokenServicesFactoryTest {
	
    private NetworkConnectionsTokenServicesFactory factory;
    
    @Before
    public void setup() {
        factory = new NetworkConnectionsTokenServicesFactory(new JdbcTemplate());
    }

    @Test
    public void getTokenServices() {
        GreenhouseUserDetails greenhouseUserDetails = new GreenhouseUserDetails(1234L, "someuser", "password", "Chuck");
        NetworkConnectionsTokenServices tokenServices = (NetworkConnectionsTokenServices) factory.getTokenServices(
                new TestingAuthenticationToken(greenhouseUserDetails, "password"), new MockHttpServletRequest());
        assertNotNull(tokenServices);
    }
}
