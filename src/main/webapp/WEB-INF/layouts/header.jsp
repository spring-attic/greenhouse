<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<div id="topbar">
	<c:if test="${currentUser == null}">
		<a href="<c:url value="/signin" />">Sign In</a>
	</c:if>
	<c:if test="${currentUser != null}">
		<s:url var="profileUrl" value="/members/{profileKey}">
			<s:param name="profileKey" value="${currentUser.profileKey}" />
		</s:url>
		<a href="${profileUrl}">${currentUser.firstName}</a> | <a href="<c:url value="/settings" />">Settings</a> | <a href="<c:url value="/signout" />">Sign Out</a>
	</c:if>
</div>
<h1>
	<a href="<c:url value="/" />">Greenhouse</a>
</h1>