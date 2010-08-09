<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="personInfo">
	<c:out value="${member.displayName}" escapeXml="true"/>
	
	<c:if test="${member.profileImageUrl != null}">
	<div id="profilePicture">
		<img src="${member.profileImageUrl}" />
	</div>	
	</c:if>
	
	<div id="connections">
		<ul>
			<li><a href="http://www.twitter.com/${connectedIds['twitter']}">At Twitter</a></li>
			<li><a href="http://www.facebook.com/profile.php?id=${connectedIds['facebook']}">At Facebook</a></li>
		</ul>
	</div>
	
	<fb:like></fb:like>
</div>