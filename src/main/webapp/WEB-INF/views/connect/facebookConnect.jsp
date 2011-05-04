<%@ page session="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>

<c:if test="${not empty message}">
	<div class="${message.type.cssClass}">${message.text}</div>
</c:if>

<form action="<c:url value="/connect/facebook" />" method="POST">
	<div class="formInfo">
		<h2>Connect to Facebook</h2>
		<p>Click the button to connect your Greenhouse account with your Facebook account.</p>
	</div>
	<input type="hidden" name="scope" value="email,publish_stream,offline_access" />
	<p><button type="submit"><img src="<c:url value="/resources/social/facebook/connect_light_medium_short.gif" />"/></button></p>
	<fieldset class="checkbox">
		<label for="postToWall"><input id="postToWall" type="checkbox" name="postToWall" /> Post a link to my Greenhouse profile on my wall after connecting</label>
	</fieldset>
	<fieldset class="checkbox">
		<label for="useProfilePicture"><input id="useProfilePicture" type="checkbox" name="useProfilePicture" /> Use my Facebook profile picture as my Greenhouse profile picture</label>
	</fieldset>
</form>
