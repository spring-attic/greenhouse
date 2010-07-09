<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="MobileTags" prefix="m" %>

<form id="signin" action="<c:url value="/signin/authenticate" />" method="post">
	<div class="header">
  		<h2>Sign in</h2>
  		<c:if test="${not empty signinErrorMessage}">
  			<div class="error">Your sign in information was incorrect.  Please try again or <a href="<c:url value="/signup" />">sign up</a>.</div>
 	 	</c:if>
	</div>	
  	<fieldset>
		<label for="login">Username or Email</label>
		<input id="login" name="j_username" type="text" size="25" <m:AppleDevice>autocorrect="off" autocapitalize="off"</m:AppleDevice> <c:if test="${not empty signinErrorMessage}">value="${SPRING_SECURITY_LAST_USERNAME}"</c:if> />
		<label for="password">Password</label>
		<input id="password" name="j_password" type="password" size="25" />
	</fieldset>
	<p><a href="<c:url value="/reset" />">Forgot your password?</a></p>
	<input type="submit" value="Sign In"/>
</form>