<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="<c:url value="/connect/twitter" />" method="POST">
	<div class="formInfo">
		<h2>Connect to Twitter</h2>
		<p>
			Click the button to connect your Greenhouse account with your Twitter account.
			You will be taken to Twitter for authorization and then will be brought back here.
		</p>
	</div>
	<input id="signin" type="image" src="<c:url value="/resources/social/twitter/signin.png" />" />
	<label for="postTweet">Tweet a link to my Greenhouse profile after connecting</label>
	<input id="postTweet" type="checkbox" name="postTweet" />
</form>