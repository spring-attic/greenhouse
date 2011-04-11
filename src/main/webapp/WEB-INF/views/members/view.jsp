<%@ page session="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>

<div id="profile" class="listing">
	<img src="${profile.pictureUrl}" alt="Profile Picture" />
	<h2><c:out value="${profile.displayName}" /></h2>
	<ul id="connectedProfiles">
		<c:forEach var="connectedProfile" items="${connectedProfiles}">
		<li><a href="${connectedProfile.url}">${connectedProfile.name}</a></li>
		</c:forEach>
	</ul>
	<fb:like></fb:like>
</div>
<div id="fb-root"></div>

<facebook:init appId="${facebookAppId}" />