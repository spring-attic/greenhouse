<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<ul>
	<c:forEach var="update" items="${updates}">
		<li>
			<c:out value="${update.text}" escapeXml="true" /> 
		</li>
	</c:forEach>
</ul>