<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form id="fb_signin" action="<c:url value="/signin/fb" />" method="post">
</form>

<form id="signin" action="<c:url value="/signin/authenticate" />" method="post">
	<div class="header">
  		<h2>Sign in</h2>
  		<c:if test="${signinError}">
  			<div class="error">Your sign in information was incorrect.  Please try again<c:if test="${!wurflDevice.isMobileBrowser}"> or <a href="<c:url value="/signup" />">sign up</a></c:if>.</div>
 	 	</c:if>
	</div>	
	<fb:login-button onlogin="$('#fb_signin').submit();" v="2" length="long">Sign in to Greenhouse using Facebook</fb:login-button>
  	<fieldset>
		<label for="login">Username or Email</label>
		<input id="login" name="j_username" type="text" size="25" <c:if test="${wurflDevice.isAppleDevice}">autocorrect="off" autocapitalize="off"</c:if> <c:if test="${not empty signinErrorMessage}">value="${SPRING_SECURITY_LAST_USERNAME}"</c:if> />
		<label for="password">Password</label>
		<input id="password" name="j_password" type="password" size="25" />
	</fieldset>
	<p><a href="<c:url value="/reset" />">Forgot your password?</a></p>
	<input type="submit" value="Sign In"/>
</form>