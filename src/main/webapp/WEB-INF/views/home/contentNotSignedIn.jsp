<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div id="intro">
	<h2>Welcome to the Greenhouse!</h2>
	<p>
		We make it fun to be an application developer.
	</p>
	<p>
		We help you connect with fellow developers and take advantage of everything the Spring community has to offer.	
	</p>
</div>
<div id="recentActivity">
	<h3>Recent Activity</h3>
	<ul id="recentActivityList" class="listings">
		<c:forEach var="item" items="${recentActivity}">
			<li>
				<img class="profile" src="${item.memberPictureUrl}" />
				<p><c:out value="${item.text}" escapeXml="true" /></p>
				<img class="activity" src="${item.imageUrl}" />
			</li>
		</c:forEach>
	</ul>
</div>