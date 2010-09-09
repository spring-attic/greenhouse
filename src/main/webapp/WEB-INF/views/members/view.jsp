<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>

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

<s:eval expression="@facebookProvider.apiKey" var="apiKey" />
<facebook:init apiKey="${apiKey}" />
