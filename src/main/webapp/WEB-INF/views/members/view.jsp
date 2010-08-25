<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="memberProfile">

	<p>
		<c:out value="${profile.displayName}" />
	</p>
	
	<div id="picture">
		<img src="${profile.pictureUrl}" alt="Profile Picture" />
	</div>	
	
	<div id="connectedProfiles">
		<ul>
			<c:forEach var="connectedProfile" items="${connectedProfiles}">
			<li>
				<a href="${connectedProfile.url}">${connectedProfile.name}</a>
			</li>
			</c:forEach>
		</ul>
	</div>
	
	<div id="fb-root"></div>	
	<fb:like></fb:like>
	
</div>