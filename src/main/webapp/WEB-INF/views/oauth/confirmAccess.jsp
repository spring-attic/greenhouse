<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:if test="${not empty sessionScope.SPRING_SECURITY_LAST_EXCEPTION}">
	<div class="error">Access could not be granted</div>
</c:if>
<c:remove scope="session" var="SPRING_SECURITY_LAST_EXCEPTION" />
<h2>Grant access</h2>

<p>The application <b>${consumer.consumerName}</b> would like the ability to read and update your data on Greenhouse.</p>

<spring:url value="/oauth/authorize" var="authorize_url" />
<form action="${authorize_url}" method="post">
	<input name="requestToken" value="${oauth_token}" type="hidden" />
	<c:if test="${!empty oauth_callback}">
		<input name="callbackURL" value="${oauth_callback}" type="hidden" />
	</c:if>
	<input name="authorize" value="Authorize" type="submit" />
</form>