<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="memberProfile">

	<c:out value="${profile.displayName}" />
	
	<div id="picture">
		<img src="<c:url value="/members/${profile.accountId}/picture?type=large"/>" 
			 onError="this.src='<c:url value="/resources/images/defaultProfilePicture.png"/>';" />
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