<%@ page import="org.springframework.security.core.AuthenticationException" %>
<%@ page import="org.springframework.security.oauth.consumer.OAuthConsumerProcessingFilter" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
        
<c:if test="${!empty sessionScope.OAUTH_FAILURE_KEY}">
    <h1>Woops!</h1>

    <p class="error"><c:out value="${message}"/></p>
    
    <p>Here's a stack trace for you:</p>
    <code><c:out value="${stackTrace}"/></code>
</c:if>
<c:remove scope="session" var="OAUTH_FAILURE_KEY"/>