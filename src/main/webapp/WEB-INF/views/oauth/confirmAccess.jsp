<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div>
  <c:if test="${!empty sessionScope.SPRING_SECURITY_LAST_EXCEPTION}">
    <div class="error">
      <h2>Woops!</h2>

      <p>Access could not be granted.</p>
    </div>
  </c:if>
  <c:remove scope="session" var="SPRING_SECURITY_LAST_EXCEPTION"/>

    <h2>Please Confirm</h2>

    <p>You hereby authorize "${consumer.consumerName}" to access the following resource:</p>

    <ul>
        <li>${consumer.resourceName} - ${consumer.resourceDescription}</li>
    </ul>

	<spring:url value="/oauth/authorize" var="authorize_url" />
    <form action="${authorize_url}" method="post">
      <input name="requestToken" value="${oauth_token}" type="hidden"/>
      <c:if test="${!empty oauth_callback}">
        <input name="callbackURL" value="${oauth_callback}" type="hidden"/>
      </c:if>
      <input name="authorize" value="Authorize" type="submit"/>
    </form>
    
</div>
