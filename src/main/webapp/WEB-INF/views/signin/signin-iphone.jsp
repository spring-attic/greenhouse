<%@ page session="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${not empty message}">
	<div class="${message.type}">${message.text}</div>
</c:if>

<h2>Sign in to your account</h2>

<form id="signin" action="<c:url value="/signin/authenticate" />" method="post">
	<div class="formInfo">
  		<c:if test="${signinError}">
  		<div class="error">
  			Your sign in information was incorrect.
  			Please try again<c:if test="${!currentDevice.mobileBrowser}"> or <a href="<c:url value="/signup" />">sign up</a></c:if>.
  		</div>
 	 	</c:if>
	</div>
	<label for="login">Username or Email</label>
	<input id="login" name="j_username" type="text" size="25" <c:if test="${currentDevice.apple}">autocorrect="off" autocapitalize="off"</c:if> <c:if test="${not empty signinErrorMessage}">value="${SPRING_SECURITY_LAST_USERNAME}"</c:if> />
	<label for="password">Password</label>
	<input id="password" name="j_password" type="password" size="25" />
	<input type="submit" value="Sign In"/> 
</form>

<p><a href="<c:url value="/reset" />">Forgot your password?</a></p>

<p>Greenhouse is a web and mobile application for Spring developers to stay socially connected to all things Spring.</p>
