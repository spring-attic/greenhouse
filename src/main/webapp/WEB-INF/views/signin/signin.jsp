<%@ page session="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>

<c:if test="${not empty message}">
	<div class="${message.type.cssClass}">${message.text}</div>
</c:if>

<form id="signin" action="<c:url value="/signin/authenticate" />" method="post">
	<div class="formInfo">
  		<h2>Greenhouse Sign In</h2>
  		<c:if test="${signinError}">
  		<div class="error">
  			Your sign in information was incorrect.
  			Please try again<c:if test="${!currentDevice.mobileBrowser}"> or <a href="<c:url value="/signup" />">sign up</a></c:if>.
  		</div>
 	 	</c:if>
	</div>
	<fieldset>
		<label for="login">Username or Email</label>
		<input id="login" name="j_username" type="text" size="25" <c:if test="${currentDevice.apple}">autocorrect="off" autocapitalize="off"</c:if> <c:if test="${not empty signinErrorMessage}">value="${SPRING_SECURITY_LAST_USERNAME}"</c:if> />
		<label for="password">Password</label>
		<input id="password" name="j_password" type="password" size="25" />	
	</fieldset>
	<p><button type="submit">Sign In</button></p>
	<p><a href="<c:url value="/reset" />">Forgot your password?</a></p>
</form>

<c:if test="${!currentDevice.mobileBrowser}">
<div id="fb-root"></div>	
<form id="fb_signin" action="<c:url value="/signin/facebook" />" method="post">
<%-- Unfortunately, offline access is the only way to get an access token that doesn't expire. 
	Facebook currently doesn't implement the refresh_token fragment of section 3.5.1 of the
	OAuth 2 specification. So, if you get a regular access token, it will expire after a
	few hours and the user will have to authenticate again. Asking for offline access allows
	the token to be long-lived, but has the unfortunate side-effect of communicating that the
	app may access their information even if they're not logged in. --%>
	<fb:login-button perms="email,publish_stream,offline_access" onlogin="$('#fb_signin').submit();" v="2" length="long">Sign in with Facebook</fb:login-button>
</form>
</c:if>

<facebook:init />