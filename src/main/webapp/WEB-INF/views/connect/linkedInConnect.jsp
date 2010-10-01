<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="<c:url value="/connect/linkedin" />" method="POST">
	<div class="formInfo">
		<h2>Connect to LinkedIn</h2>
		<p>Click the button to connect your Greenhouse account with your LinkedIn account.</p>
	</div>
	<input id="signin" type="image" src="<c:url value="/resources/social/linkedin/connectWithLinkedIn_small.png" />" />
</form>