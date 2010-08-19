<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<ul id="recentActivityList">
	<c:forEach var="item" items="${recentActivity}">
		<li>
			<img class="profile" src="${item.memberPictureUrl}" />
			<p>
				<c:out value="${item.text}" escapeXml="true" />
			</p>
			<img class="activity" src="${item.imageUrl}" />
		</li>
	</c:forEach>
</ul>