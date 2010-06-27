<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<p>Settings</p>

<div id="twitter">
	<s:url value="/settings/twitter" var="twitterConnectUrl"/>
	<a href="${twitterConnectUrl}">Connect to Twitter</a>
</div>