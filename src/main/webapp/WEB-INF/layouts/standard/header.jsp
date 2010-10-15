<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<h1><a href="<c:url value="/" />"><img src="<c:url value="/resources/header-logo.png" />" alt="Greenhouse" /></a></h1>
<div id="topbar">
	<c:if test="${account == null}">
	<a href="<c:url value="/signin" />">Sign In</a>
	</c:if>
	<c:if test="${account != null}">
	<a href="<c:url value="/members/${account.profileId}" />"><c:out value="${account.firstName}" /></a> | <a href="<c:url value="/invite" />">Invite</a> | <a href="<c:url value="/events" />">Events</a> | <a href="<c:url value="/develop/apps" />">Develop</a> | <a href="<c:url value="/settings" />">Settings</a> | <a href="<c:url value="/signout" />">Sign Out</a>
	</c:if>
</div>
