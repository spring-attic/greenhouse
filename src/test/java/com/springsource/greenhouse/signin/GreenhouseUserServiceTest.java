package com.springsource.greenhouse.signin;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.springsource.greenhouse.signin.GreenhouseUserService.UserDetailsMapper;


public class GreenhouseUserServiceTest {
    private JdbcTemplate jdbcTemplate;
    private GreenhouseUserService service;
    
    @Before
    public void setup() {
        jdbcTemplate = mock(JdbcTemplate.class);
        service = new GreenhouseUserService(jdbcTemplate);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void shouldLoadUserByEmail() {
        GreenhouseUserDetails userDetails = new GreenhouseUserDetails(1234L, "testUser", "password", "Chuck");
        when(jdbcTemplate.queryForObject(eq(GreenhouseUserService.SELECT_USER_BY_EMAIL), any(RowMapper.class), eq("a@b.com"))).thenReturn(userDetails);        
        assertSame(userDetails, service.loadUserByUsername("a@b.com"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldLoadUserByUsername() {
        GreenhouseUserDetails userDetails = new GreenhouseUserDetails(1234L, "testUser", "password", "Chuck");
        when(jdbcTemplate.queryForObject(eq(GreenhouseUserService.SELECT_USER_BY_USERNAME), any(RowMapper.class), eq("testUser"))).thenReturn(userDetails);        
        assertSame(userDetails, service.loadUserByUsername("testUser"));
    }
    
    @Test(expected=UsernameNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void shouldThrowUsernameNotFoundExceptionWhenNoSuchUserExistsForEmail() {
        when(jdbcTemplate.queryForObject(eq(GreenhouseUserService.SELECT_USER_BY_EMAIL), any(RowMapper.class), eq("stranger@danger.com"))).thenThrow(new EmptyResultDataAccessException(1));
        service.loadUserByUsername("stranger@danger.com");
    }
    
    @Test(expected=UsernameNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void shouldThrowUsernameNotFoundExceptionWhenNoSuchUserExistsForUsername() {
        when(jdbcTemplate.queryForObject(eq(GreenhouseUserService.SELECT_USER_BY_USERNAME), any(RowMapper.class), eq("stranger"))).thenThrow(new EmptyResultDataAccessException(1));
        service.loadUserByUsername("stranger");
    }
    
    @Test
    public void shouldMapResultsToGreenhouseUserDetails() throws SQLException {
        UserDetailsMapper mapper = new UserDetailsMapper("testUser");
        ResultSet rs = mock(ResultSet.class);
        when(rs.getLong("id")).thenReturn(1234L);
        when(rs.getString("password")).thenReturn("somePassword");
        when(rs.getString("firstName")).thenReturn("Chuck");
        
        GreenhouseUserDetails userDetails = mapper.mapRow(rs , 1);
        assertEquals(1234L, userDetails.getEntityId().longValue());
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("somePassword", userDetails.getPassword());
        assertEquals("Chuck", userDetails.getFirstName());
    }
}
