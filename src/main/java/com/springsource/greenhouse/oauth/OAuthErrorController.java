package com.springsource.greenhouse.oauth;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth.consumer.OAuthConsumerProcessingFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OAuthErrorController {
    @RequestMapping("/oauthError")
    public String displayOAuthError(HttpSession session, Map<String, Object> model) {
        
        AuthenticationException authenticationException = (AuthenticationException) session.getAttribute(OAuthConsumerProcessingFilter.OAUTH_FAILURE_KEY);
        model.put("message", authenticationException.getMessage());
        
        StackTraceElement[] stackTraceElements = authenticationException.getStackTrace();
        StringBuilder stackTraceBuilder = new StringBuilder();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            stackTraceBuilder.append(stackTraceElement.toString() + "\n");
        }
        
        model.put("stackTrace", stackTraceBuilder.toString());
        
        return "oauth/error";
    }
}
