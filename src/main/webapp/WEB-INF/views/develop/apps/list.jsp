<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags/develop/apps" prefix="apps" %>

<c:forEach var="appSummary" items="${appSummaryList}">
	<apps:summary value="${appSummary}" />
</c:forEach>

<ul>
	<li><a href="<c:url value="/develop/apps/new" />">Register an App</a></li>
</ul>
