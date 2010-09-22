<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>

<c:if test="${not empty message}">
	<div class="${message.type}">${message.text}</div>
</c:if>

<form id="disconnect" method="post">
	<div class="header">
		<p>
			Your Greenhouse account is connected to your Facebook account.
			Click the button if you wish to disconnect.
		</p>
	</div>
	<input type="submit" value="Disconnect" onclick="FB.logout(function(response) { return true; } );" />
	<input type="hidden" name="_method" value="DELETE" />
</form>

<s:eval expression="@facebookAccountProvider.apiKey" var="apiKey" />
<facebook:init apiKey="${apiKey}" />