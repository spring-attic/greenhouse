<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="personInfo">
	<c:out value="${member.displayName}" escapeXml="true"/>
	
	<div id="connections">
		<ul>
			<li><a href="http://www.twitter.com/${connectedIds['twitter']}">At Twitter</a></li>
			<li><a href="http://www.facebook.com/profile.php?id=${connectedIds['facebook']}">At Facebook</a></li>
		</ul>
	</div>
	
	<fb:like></fb:like>
</div>