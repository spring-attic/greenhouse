<html
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jstl/core_rt" 
    xmlns:authz="http://www.springframework.org/security/tags"
    xmlns:spring="http://www.springframework.org/tags">
  <jsp:directive.page contentType="text/html;charset=UTF-8" />  

  <head>
     <title>Authorization</title>
     <meta name = "viewport" content = "width = device-width"/>
  </head>
 <body>
	<div style="font-family:verdana;color:#3E7D00;">
	
	  <c:if test="${!empty sessionScope.SPRING_SECURITY_LAST_EXCEPTION}">
	    <div class="error">
	      <h2>Woops!</h2>
	
	      <p>Access could not be granted.</p>
	    </div>
	  </c:if>
	  <c:remove scope="session" var="SPRING_SECURITY_LAST_EXCEPTION"/>
	
	    <p><b>May I, please...?</b></p>
	
	    <p>"${consumer.consumerName}" is asking for your permission to access the following details:</p>
	
	    <ul>
	        <li><b>${consumer.resourceName}</b> - ${consumer.resourceDescription}</li>
	    </ul>
	
		<spring:url value="/oauth/authorize" var="authorize_url" />
	    <form action="${authorize_url}" method="post">
	      <input name="requestToken" value="${oauth_token}" type="hidden"/>
	      <c:if test="${!empty oauth_callback}">
	        <input name="callbackURL" value="${oauth_callback}" type="hidden"/>
	      </c:if>
	      <label><input name="authorize" value="Yes, allow it" type="submit" style="font-size:12pt;"/></label>
	    </form>
	</div>
 </body>
</html>
