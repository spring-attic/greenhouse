<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<form id="disconnect" method="post">
	<div id="header">
		<c:if test="${not empty message}">
			<div class="${message.type}">${message.text}</div>
		</c:if>
	</div>

    <p>Your Greenhouse account is currently linked to Facebook as <fb:name linked="false" useyou="false" uid="${facebookUserId}"></fb:name>.
          
	<input type="submit" value="Disconnect" onclick="FB.logout(function(response) { return true; } );" />
	<input type="hidden" name="_method" value="DELETE" />
</form>
