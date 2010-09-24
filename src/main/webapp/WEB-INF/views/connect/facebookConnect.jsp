<%@ page session="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>

<c:if test="${not empty message}">
	<div class="${message.type}">${message.text}</div>
</c:if>

<form id="fb_signin" action="<c:url value="/connect/facebook" />" method="post">
	<div class="formInfo">
		<h2>Connect to Facebook</h2>
		<p>Click the button to connect your Greenhouse account with your Facebook account.</p>
	</div>
	<div id="fb-root"></div>	
	<fb:login-button perms="email,publish_stream,offline_access" onlogin="$('#fb_signin').submit();" v="2" length="long">Connect to Facebook</fb:login-button><br/><br/>
	<label for="postToWall">Post a link to my Greenhouse profile on my wall after connecting</label>
	<input id="postToWall" type="checkbox" name="postToWall" />
	<label for="useProfilePicture">Use my Facebook profile picture as my Greenhouse profile picture</label>
	<input id="useProfilePicture" type="checkbox" name="useProfilePicture" />
</form>

<facebook:init />
