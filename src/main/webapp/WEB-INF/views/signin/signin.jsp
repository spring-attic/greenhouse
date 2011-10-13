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
  		<c:if test="${not empty param['error']}">
  		<div class="error">
  			Your sign in information was incorrect.
  			Please try again<c:if test="${!currentDevice.mobile}"> or <a href="<c:url value="/signup" />">sign up</a></c:if>.
  		</div>
 	 	</c:if>
	</div>
	<fieldset>
		<label for="login">Username or Email</label>
		<input id="login" name="j_username" type="text" size="25" autocorrect="off" autocapitalize="off" <c:if test="${not empty signinErrorMessage}">value="${SPRING_SECURITY_LAST_USERNAME}"</c:if> />
		<label for="password">Password</label>
		<input id="password" name="j_password" type="password" size="25" />	
	</fieldset>
	<p><button type="submit">Sign In</button></p>
	<p><a href="<c:url value="/reset" />">Forgot your password?</a></p>
</form>

<c:if test="${!currentDevice.mobile}">
<form id="facebook_signin" action="<c:url value="/signin/facebook" />" method="post">
	<button type="submit">Sign in with Facebook</button>
</form>
<form id="twitter_signin" action="<c:url value="/signin/twitter" />" method="post">
	<button type="submit">Sign in with Twitter</button>
</form>
<form id="linkedin_signin" action="<c:url value="/signin/linkedin" />" method="post">
	<button type="submit">Sign in with LinkedIn</button>
</form>
</c:if>