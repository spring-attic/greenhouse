<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<h2>Settings</h2>

<div id="apps">
	<h3>Connected Apps</h3>
	<ul>
	<c:forEach var="app" items="${apps}">
		<li>
			${app.name}
			<form action="settings/apps/${app.accessToken}" method="post">
				<p>
					<button type="submit">Disconnect</button>
				</p>
				<input type="hidden" name="_method" value="DELETE" />
			</form>
		</li>
	</c:forEach>
	</ul>
</div>

<div id="connectedAccounts">
	<h3>Connected Accounts</h3>
	<div id="twitter">
		<h4>Twitter</h4>
		<a href="<s:url value="/connect/twitter" />">Connect to Twitter</a>	
	</div>
	<div id="facebook">
		<h4>Facebook</h4>
		<a href="<s:url value="/connect/facebook" />">Connect to Facebook</a>	
	</div>
</div>