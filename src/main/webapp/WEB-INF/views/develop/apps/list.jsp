<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib tagdir="/WEB-INF/tags/develop/apps" prefix="apps" %>

<h2>Greenhouse Apps</h2>

<c:if test="${fn:length(appSummaryList) == 0}">
<p>You have not registered any apps.</p>
</c:if>
<c:if test="${fn:length(appSummaryList) > 0}">
<p>You have registered the following apps:</p>
</c:if>

<c:forEach var="appSummary" items="${appSummaryList}">
<ul class="listings">
	<li><apps:summary value="${appSummary}" /></li>
</ul>
</c:forEach>

<a href="<c:url value="/develop/apps/new" />">Register App</a>