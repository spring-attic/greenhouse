<%@ page session="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>

<c:if test="${not empty message}">
	<div class="${message.type.cssClass}">${message.text}</div>
</c:if>

<script>
	function signInWithFacebook() {
		FB.getLoginStatus(function(response) {
	        if (response.session) {
	    		$('#fb_signin').submit();
	        }
	      });

	}
</script>

<form id="fb_signin" action="<c:url value="/connect/facebook" />" method="post">
	<div class="formInfo">
		<h2>Connect to Facebook</h2>
		<p>Click the button to connect your Greenhouse account with your Facebook account.</p>
	</div>
	<div id="fb-root"></div>	
	<p><fb:login-button perms="email,publish_stream,offline_access" onlogin="signInWithFacebook();" v="2" length="long">Connect to Facebook</fb:login-button></p>
	<fieldset class="checkbox">
		<label for="postToWall"><input id="postToWall" type="checkbox" name="postToWall" /> Post a link to my Greenhouse profile on my wall after connecting</label>
	</fieldset>
	<fieldset class="checkbox">
		<label for="useProfilePicture"><input id="useProfilePicture" type="checkbox" name="useProfilePicture" /> Use my Facebook profile picture as my Greenhouse profile picture</label>
	</fieldset>
</form>

<facebook:init />
