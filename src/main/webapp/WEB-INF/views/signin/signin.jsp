<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form id="signin" action="<c:url value="/signin/authenticate" />" method="post">
  	<fieldset>
  		<legend>Sign in</legend>
		<label for="login">Username or Email</label>
		<input id="login" name="j_username" type="text" size="25" />
		<label for="password">Password</label>
		<input id="password" name="j_password" type="password" size="12" />
		<input type="submit" value="Sign In"/>
	</fieldset>
</form>