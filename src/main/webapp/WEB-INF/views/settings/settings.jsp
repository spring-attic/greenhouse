<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<h2>Settings</h2>

<h3>Connected Accounts</h3>
<ul id="connectedAccounts" class="listings">
	<li class="listing">
		<h4><img src="<c:url value="/resources/social/twitter/logo.png" />" alt="Twitter"></h4>
		<a href="<c:url value="/connect/twitter" />">Connect to Twitter</a>	
	</li>
	<li class="listing">
		<h4><img src="<c:url value="/resources/social/facebook/logo.png" />" alt="Facebook"></h4>
		<a href="<c:url value="/connect/facebook" />">Connect to Facebook</a>	
	</li>
	<li class="listing">
		<h4><img src="<c:url value="/resources/social/linkedin/logo.png" />" alt="Linked In"></h4>
		<a href="<c:url value="/connect/linkedin" />">Connect to LinkedIn</a>	
	</li>
	<li class="listing">
		<h4><img src="<c:url value="/resources/social/tripit/logo.png" />" alt="TripIt"></h4>
		<a href="<c:url value="/connect/tripit" />">Connect to TripIt</a>	
	</li>
</ul>

<div id="connectedApps">
	<h3>Connected Apps</h3>
	<ul class="listings">
		<c:forEach var="app" items="${apps}">
		<li>
			<h4>${app.name}</h4>
			<form action="settings/apps/${app.accessToken}" method="post">
				<p><button type="submit">Disconnect</button></p>
				<input type="hidden" name="_method" value="DELETE" />
			</form>
		</li>
		</c:forEach>
	</ul>
</div>
