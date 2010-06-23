<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<form:form id="signup" method="post" modelAttribute="signupForm">
  	<fieldset>
  		<legend>Sign up</legend>
  		
  		<form:label path="firstName">First Name</form:label>
  		<form:input path="firstName" />
  		
  		<form:label path="lastName">Last Name</form:label>
  		<form:input path="lastName" />
  		
  		<form:label path="email">Email</form:label>
  		<form:input path="email" />
  		
  		<form:label path="password">Password</form:label>
  		<form:password path="password" />
  		
  		<form:label path="confirmPassword">Confirm Password</form:label>
  		<form:password path="confirmPassword" />
  		
		<input type="submit" value="Sign up">
		
	</fieldset>
</form:form>