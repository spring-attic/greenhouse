<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h3>Find Results</h3>
<c:choose>
<c:when test="${not empty friends}">
<ul>
<c:forEach var="friend" items="${friends}">
	<li><a href="<c:url value="/members/${friend.id}" />">${friend.label}</a></li>
</c:forEach>
</ul>
</c:when>
<c:when test="${empty friends}">
<p>No friends found</p>
</c:when>
</c:choose>