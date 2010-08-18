<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<ul id="recentActivityList">
	<c:forEach var="item" items="${recentActivity}">
		<li>
			<img class="profile" src="${item.memberPictureUrl}" />
			<c:out value="${item.text}" escapeXml="true" />
			<img class="badge" src="${item.imageUrl}" />
		</li>
	</c:forEach>
</ul>