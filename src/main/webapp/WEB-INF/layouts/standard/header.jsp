<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h1><a title="Greenhouse" href="<c:url value="/" />"><img src="<c:url value="/resources/logo-header.png" />" alt="Greenhouse" /></a></h1>
<div id="nav">
	<ul>
		<c:if test="${account == null}">
		<li><a href="<c:url value="/signin" />">Sign In</a></li>
		</c:if>
		<c:if test="${account != null}">
		<li><a href="<c:url value="/members/${account.profileId}" />"><c:out value="${account.firstName}" /></a></li>
		<li><a href="<c:url value="/invite" />">Invite</a></li>
		<li><a href="<c:url value="/events" />">Events</a></li>
		<li><a href="<c:url value="/develop/apps" />">Develop</a></li>
		<li><a href="<c:url value="/settings" />">Settings</a></li>
		<li><a href="<c:url value="/signout" />">Sign Out</a></li>
		</c:if>	
	</ul>
</div>
