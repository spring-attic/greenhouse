<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/urls" prefix="u" %>

<div>
	<c:if test="${not empty friends}">
	<h2>Some of your Facebook friends are already Greenhouse members</h2>
	<ul>
		<c:forEach var="friend" items="${friends}">
		<li>
			<a href="<s:url value="/members/${friend.username}" />">${friend.name}</a>
		</li>
		</c:forEach>
	</ul>
	<h2>Invite more Facebook friends</h2>
	</c:if>
<fb:serverfbml style="width: 625px;">
	<script type="text/fbml">
		<fb:fbml>
			<fb:request-form action='<u:absoluteUrl value="/invite/facebook" />?skip=1' method="POST" invite="true" type="Greenhouse" content="Join me in the Greenhouse! It's the best way to exchange ideas and information with other Spring application developers. <fb:req-choice url='http://greenhouse.springsource.org' label='Join the Greenhouse' />">
				<fb:multi-friend-selector bypass="cancel" email_invite="false" import_external_friends="false" cols="4" showborder="false" actiontext="Invite your Facebook friends to join the Greenhouse"></fb:multi-friend-selector>
			</fb:request-form>
		</fb:fbml>
	</script>
</fb:serverfbml>
</div>        
