<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form id="fb_signin" action="<c:url value="/settings/facebook" />" method="post">
	<div id="header">
		<div class="info">
			Click the button to connect your Greenhouse account with your Facebook account.
			If you're not already logged into Facebook, you will be shown a dialog 
			to authorize Greenhouse to access your Facebook profile.
		</div>
	</div>
	<fb:login-button perms="email,publish_stream,offline_access" onlogin="$('#fb_signin').submit();" v="2" length="long">Connect to Facebook</fb:login-button>
	<br/><br/>
	<label>Post a link to my Greenhouse profile on my wall after connecting</label>
	<input type="checkbox" name="postIt" />
	
</form>
