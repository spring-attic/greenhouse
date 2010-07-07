<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<ul>
	<c:forEach var="update" items="${updates}">
		<li>
			${update.text} 
		</li>
	</c:forEach>
</ul>