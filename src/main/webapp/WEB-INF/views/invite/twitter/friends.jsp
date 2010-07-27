<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<h2>Find Results</h2>
<c:choose>
	<c:when test="${not empty friends}">
		<ul>
			<c:forEach var="friend" items="${friends}">
				<li>
					<a href="<s:url value="/members/${friend.username}" />">${friend.name}</a>
				</li>
			</c:forEach>
		</ul>
	</c:when>
	<c:when test="${empty friends}">
		<p>No friends found</p>
	</c:when>
</c:choose>
