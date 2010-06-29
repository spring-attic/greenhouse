<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<p>Settings</p>

<div id="apps">
	<h2>Connected Applications</h2>
	<table>
		<c:forEach var="app" items="${apps}">
			<tr>
				<td>${app.name}</td>
				<td>
					<form action="apps/${app.accessToken}">
						<input type="submit" value="Disconnect" />
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>

<div id="connectedAccounts">
	<h2>Connected Accounts</h2>
	<div id="twitter">
		<h3>Twitter</h3>
		<a href="<s:url value="/settings/twitter" />">Connect to Twitter</a>	
	</div>
</div>