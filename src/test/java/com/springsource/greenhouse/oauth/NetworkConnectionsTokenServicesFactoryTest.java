package com.springsource.greenhouse.oauth;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;

import com.springsource.greenhouse.signin.GreenhouseUserDetails;

public class NetworkConnectionsTokenServicesFactoryTest {
    private JdbcTemplate jdbcTemplate;
    private NetworkConnectionsTokenServicesFactory factory;
    
    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate();
        factory = new NetworkConnectionsTokenServicesFactory(jdbcTemplate);
        Assert.assertSame(jdbcTemplate, factory.getJdbcTemplate());
    }

    @Test
    public void shouldCreateANetworkConnectionsTokenServicesObject() {
        GreenhouseUserDetails greenhouseUserDetails = new GreenhouseUserDetails(1234L, "someuser", "password", "Chuck");
        NetworkConnectionsTokenServices tokenServices = (NetworkConnectionsTokenServices) factory.getTokenServices(
                new TestingAuthenticationToken(greenhouseUserDetails, "password"), new MockHttpServletRequest());
        
        Assert.assertSame(jdbcTemplate, tokenServices.getJdbcTemplate());
        Assert.assertEquals(1234L, tokenServices.getUserId().longValue());
    }
}
