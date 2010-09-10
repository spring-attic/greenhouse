<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags/develop/apps" prefix="apps" %>

<apps:summary value="${app.summary}" />

<dl>
	<dt>API key</dt>
	<dd>${app.apiKey}</dd>
	<dt>Secret</dt>
	<dd>${app.secret}</dd>
</dl>

<form id="deleteApp" action="<c:url value="/develop/apps/${app.summary.slug}" />" method="post">
	<input type="hidden" name="_method" value="DELETE" />
	<button>Delete</button>
</form>

<ul>
	<li><a href="<c:url value="/develop/apps/edit/${app.summary.slug}" />">Edit details</a></li>
</ul>
