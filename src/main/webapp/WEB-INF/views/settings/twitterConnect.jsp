<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="<c:url value="/settings/twitterconnect/authorize" />">
	<div id="header">
		<div class="info">
			Click the button to connect your Greenhouse account with your Twitter account.
			You will be sent to Twitter's website for authorization and then will be brought back here.
		</div>
	</div>
	<input type="image" src="<c:url value="/resources/social/twitter/signin.png" />" />
	<label>Tweet a link to my Greenhouse profile after connecting</label>
	<input type="checkbox" name="tweetIt" />
</form>