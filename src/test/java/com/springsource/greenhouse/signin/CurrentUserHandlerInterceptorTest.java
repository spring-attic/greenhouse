package com.springsource.greenhouse.signin;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;


public class CurrentUserHandlerInterceptorTest {
	
    private CurrentUserHandlerInterceptor interceptor;
    
    private GreenhouseUserDetails userDetails;
    
    @Before
    public void setup() {
        interceptor = new CurrentUserHandlerInterceptor();
        userDetails = new GreenhouseUserDetails(1234L, "testUser", "password", "Chuck");
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(userDetails, "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    @Test
    public void preHandleShouldDoNothingInteresting() throws Exception {
        assertTrue(interceptor.preHandle(null, null, null));
    }
    
    @Test
    public void postHandleShouldAddCurrentUserToModel() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        interceptor.postHandle(null, null, null, modelAndView);            
        assertSame(userDetails, modelAndView.getModelMap().get("currentUser"));
    }
    
    @Test
    public void postHandleShouldNotAddCurrentUserToModelIfModelAndViewIsNull() throws Exception {
        interceptor.postHandle(null, null, null, null);            
    }
    
    @Test
    public void postHandleShouldNotAddCurrentUserToModelIfAuthenticationIsNotInSecurityContext() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        SecurityContextHolder.getContext().setAuthentication(null);
        interceptor.postHandle(null, null, null, modelAndView);  
        assertNull(modelAndView.getModelMap().get("currentUser"));
    }
    
    @Test
    public void postHandleShouldNotAddCurrentUserToModelIfAuthenticationIsInstanceOfAnonymousAuthenticationToken() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        List<GrantedAuthority> anonymousAuthorities = Arrays.asList(new GrantedAuthority[] {new GrantedAuthorityImpl("ROLE_ANONYMOUS")});
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("key", "principal", anonymousAuthorities));
        interceptor.postHandle(null, null, null, modelAndView);  
        assertNull(modelAndView.getModelMap().get("currentUser"));
    }
    
    @Test
    public void shouldDoNothingAfterCompletion() throws Exception {
        interceptor.afterCompletion(null, null, null, null);
    }
}
