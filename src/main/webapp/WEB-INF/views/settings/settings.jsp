<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<p>Settings</p>

<div id="apps">
	<h2>Connected Apps</h2>
	<ul>
		<c:forEach var="app" items="${apps}">
			<li>
				${app.name}
				<form action="settings/apps/${app.accessToken}" method="post">
					<input type="submit" value="Disconnect" />
					<input type="hidden" name="_method" value="DELETE" />
				</form>
			</li>
		</c:forEach>
	</ul>
</div>

<div id="connectedAccounts">
	<h2>Connected Accounts</h2>
	<div id="twitter">
		<h3>Twitter</h3>
		<a href="<s:url value="/settings/twitter" />">Connect to Twitter</a>	
	</div>
</div>