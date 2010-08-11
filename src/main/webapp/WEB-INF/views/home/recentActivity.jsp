<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<ul>
	<c:forEach var="item" items="${recentActivity}">
		<li>
			<c:out value="${item.text}" escapeXml="true" /> 
		</li>
	</c:forEach>
</ul>