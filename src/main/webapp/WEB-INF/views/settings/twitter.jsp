<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<p>${connectedMessage}</p>

<p>To connect your Greenhouse account with your Twitter account, click on the button below.</p>

<s:url value="/settings/twitterconnect/authorize" var="authorizeUrl"/>
<s:url value="/resources/images/networks/twitterSignIn.png" var="twitterSignInImage"/>
<a href="${authorizeUrl}"><img src="${twitterSignInImage}" border="0"/></a>
            
<p>This will send you Twitter's website. Once there, press the "Allow" button and you'll be brought back here.</p>