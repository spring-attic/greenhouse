<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags/develop/apps" prefix="apps" %>

<h2>App Settings</h2>

<apps:summary value="${app.summary}" />

<dl>
	<dt>API key</dt>
	<dd>${app.apiKey}</dd>
	<dt>Secret</dt>
	<dd>${app.secret}</dd>
</dl>

<form id="deleteApp" action="<c:url value="/develop/apps/${slug}" />" method="post">
	<input type="hidden" name="_method" value="DELETE" />
	<p><button type="submit">Delete</button></p>
</form>

<a href="<c:url value="/develop/apps/edit/${slug}" />">Edit details</a>
