<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<s:url value="/settings/twitterconnect/authorize" var="authorizeUrl"/>
<s:url value="/resources/images/networks/twitterSignIn.png" var="twitterSignInImage"/>

<form action="${authorizeUrl}">
	<div id="header">
		<div class="info">
			Click the button to connect your Greenhouse account with your Twitter account.
			You will be sent to Twitter's website for authorization and then will be brought back here.
		</div>
	</div>
	<input type="image" src="${twitterSignInImage}" />
	<label>Tweet a link to my Greenhouse profile after connecting</label>
	<input type="checkbox" name="tweetIt" />
</form>