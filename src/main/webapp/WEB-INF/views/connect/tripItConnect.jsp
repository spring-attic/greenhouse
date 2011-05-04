<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="<c:url value="/connect/tripit" />" method="POST">
	<div class="formInfo">
		<h2>Connect to TripIt</h2>
		<p>
			Click the button to connect your Greenhouse account with your TripIt account.
			You will be taken to TripIt for authorization and then will be brought back here.
		</p>
	</div>
	<input type="submit" id="signin" value="Connect with TripIt"/>
</form>