<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<ul>
	<c:forEach var="friend" items="${friends}">
		<li>
			<a href="<s:url value="/members/${friend.username}" />">${friend.name}</a>
		</li>
	</c:forEach>
</ul>