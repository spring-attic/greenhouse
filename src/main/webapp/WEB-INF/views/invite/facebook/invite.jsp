<%@ page session="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>
<%@ taglib tagdir="/WEB-INF/tags/urls" prefix="u" %>

<h2>Invite Facebook Friends</h2>

<c:if test="${not empty friends}">
<p>Some of your friends are already in the Greenhouse</p>
<ul>
	<c:forEach var="friend" items="${friends}">
	<li><a href="<s:url value="/members/${friend.id}" />">${friend.label}</a></li>
	</c:forEach>
</ul>
<h5>Invite more...</h5>
</c:if>

<c:if test="${empty friends}">
<p>None of your friends have Greenhouse accounts connected to Facebook</p>
<h5>Invite some...</h5>
</c:if>

<fb:serverfbml style="width: 625px;">
	<script type="text/fbml">
		<fb:fbml>
			<fb:request-form action='<u:absoluteUrl value="/invite/facebook/request-form" />' method="POST" invite="true" type="Greenhouse" content="Join me in the Greenhouse! It's the best way to exchange ideas and information with other Spring application developers. <fb:req-choice url='http://greenhouse.springsource.org' label='Join the Greenhouse' />">
				<fb:multi-friend-selector bypass="cancel" email_invite="false" import_external_friends="false" cols="4" showborder="false" actiontext="Invite your Facebook friends to join Greenhouse"></fb:multi-friend-selector>
			</fb:request-form>
		</fb:fbml>
	</script>
</fb:serverfbml>

<facebook:init appId="${facebookAppId}" />
