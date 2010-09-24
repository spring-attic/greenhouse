<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>
<%@ taglib tagdir="/WEB-INF/tags/urls" prefix="u" %>

<h2>Invite Facebook Friends</h2>

<c:if test="${not empty friendAccounts}">
<h3>Some of your friends are already in the Greenhouse</h3>
<ul>
	<c:forEach var="friendAccount" items="${friendAccounts}">
	<li>
		<a href="<s:url value="/members/${friendAccount.memberProfileKey}" />">${friendAccount.fullName}</a>
	</li>
	</c:forEach>
</ul>
<h3>Invite more...</h3>
</c:if>

<c:if test="${empty friendAccounts}">
<h3>None of your friends have Greenhouse accounts connected to Facebook</h3>
<h3>Invite some...</h3>
</c:if>

<fb:serverfbml style="width: 625px;">
	<script type="text/fbml">
		<fb:fbml>
			<fb:request-form action='<u:absoluteUrl value="/invite/facebook" />?skip=1' method="POST" invite="true" type="Greenhouse" content="Join me in the Greenhouse! It's the best way to exchange ideas and information with other Spring application developers. <fb:req-choice url='http://greenhouse.springsource.org' label='Join the Greenhouse' />">
				<fb:multi-friend-selector bypass="cancel" email_invite="false" import_external_friends="false" cols="4" showborder="false" actiontext="Invite your Facebook friends to join Greenhouse"></fb:multi-friend-selector>
			</fb:request-form>
		</fb:fbml>
	</script>
</fb:serverfbml>

<facebook:init />