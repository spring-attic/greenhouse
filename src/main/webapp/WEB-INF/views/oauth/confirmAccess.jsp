<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>Grant access</h2>

<p>The application <b>${consumer.consumerName}</b> would like the ability to read and update your data on Greenhouse.</p>

<c:url value="/oauth/authorize" var="authorize_url" />
<form action="${authorize_url}" method="post">
	<input name="requestToken" value="${oauth_token}" type="hidden" />
	<c:if test="${!empty oauth_callback}">
		<input name="callbackURL" value="${oauth_callback}" type="hidden" />
	</c:if>
	<p>
		<button type="submit">Authorize</button>
	</p>
</form>