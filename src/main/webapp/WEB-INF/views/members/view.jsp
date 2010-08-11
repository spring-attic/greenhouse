<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="memberProfile">

	<c:out value="${profile.displayName}" />
	
	<c:if test="${profile.pictureUrl != null}">
	<div id="picture">
		<img src="${profile.pictureUrl}" />
	</div>	
	</c:if>
	
	<div id="connectedProfiles">
		<ul>
			<c:forEach var="connectedProfile" items="${connectedProfiles}">
			<li>
				<a href="${connectedProfile.url}">${connectedProfile.name}</a>
			</li>
			</c:forEach>
		</ul>
	</div>
	
	<fb:like></fb:like>
	
</div>