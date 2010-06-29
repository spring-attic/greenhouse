<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<p>To connect your Greenhouse account with your Twitter account, click on the button below.</p>

<s:url value="/settings/twitterconnect/authorize" var="authorizeUrl"/>
<s:url value="/resources/images/networks/twitterSignIn.png" var="twitterSignInImage"/>

<form method="GET" action="${authorizeUrl}">
  <input type="image" src="${twitterSignInImage}"/>
  <p>Do you want to tweet that you're in the Greenhouse?</p>
  <p><input type="checkbox" name="tweetIt" value="true" style="display:inline;">Yes</input></p>
</form>

                        
<p>This will send you Twitter's website. Once there, press the "Allow" button and you'll be brought back here.</p>