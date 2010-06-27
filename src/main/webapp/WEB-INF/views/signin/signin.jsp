<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form id="signin" class="cleanform" action="<c:url value="/signin/authenticate" />" method="post">
	<div class="formInfo">
  		<h3>Sign in</h3>
  		<c:if test="${not empty signinErrorMessage}">
  			<p class="error">The sign in information you entered was incorrect.  Please try again or <a href="<c:url value="/signup" />">sign up</a>.</p>
 	 	</c:if>
	</div>  	
  	<fieldset>
		<label for="login">Username or Email</label>
		<input id="login" name="j_username" type="text" size="25" />
		<label for="password">Password</label>
		<input id="password" name="j_password" type="password" size="12" />
	</fieldset>
	<input type="submit" value="Sign In"/>
</form>