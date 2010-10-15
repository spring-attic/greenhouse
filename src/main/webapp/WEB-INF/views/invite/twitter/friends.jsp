<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h3>Find Results</h3>
<c:choose>
<c:when test="${not empty friendAccounts}">
<ul>
<c:forEach var="friendAccount" items="${friendAccounts}">
	<li><a href="<c:url value="/members/${friendAccount.id}" />">${friendAccount.label}</a></li>
</c:forEach>
</ul>
</c:when>
<c:when test="${empty friendAccounts}">
<p>No friends found</p>
</c:when>
</c:choose>