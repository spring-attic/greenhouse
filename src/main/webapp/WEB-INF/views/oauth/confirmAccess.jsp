<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>Grant access</h2>

<p>The application <b>${clientApp.summary.name}</b> would like the ability to read and update your data on Greenhouse.</p>

<c:url value="/oauth/authorize" var="authorize_url" />
<form action="${authorize_url}" method="post">
	<input name="user_oauth_approval" value="true" type="hidden" />
	<p><button type="submit">Authorize</button></p>
</form>