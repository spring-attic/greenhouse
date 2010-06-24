<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<p>Settings</p>

<s:url value="/settings/twitterconnect" var="twitterConnectUrl"/>
<a href="${twitterConnectUrl}">Connect to Twitter</a>