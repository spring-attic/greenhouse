<div xmlns:page="urn:jsptagdir:/WEB-INF/tags/form" xmlns:c="http://java.sun.com/jstl/core_rt" xmlns:authz="http://www.springframework.org/security/tags"
      xmlns:spring="http://www.springframework.org/tags">

    <page:page id="title" title="Authorize">

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
    
    </page:page>
</div>
