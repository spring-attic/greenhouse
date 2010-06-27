<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<p>Sign in</p>
<form id="signin" action="<c:url value="/signin/authenticate" />" method="post">	
	<div class="signup-field-head">Username or Email</div>
	<div class="signup-field">
		<input id="login" name="j_username" type="text" />
	</div>
	<div class="signup-field-head">Password</div>
	<div class="signup-field">
		<input id="password" name="j_password" type="password" />
	</div>
	<div align="center"></div>
	<br>
	<input type="submit" value="Sign In"/>
</form>