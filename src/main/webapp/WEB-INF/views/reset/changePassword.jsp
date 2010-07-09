<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<form:form id="resetPassword" method="post" modelAttribute="changePasswordForm">
	<div class="header">
  		<h2>Reset password</h2>
	</div>
  	<fieldset>
  		<form:label path="password">
  			Password (at least 6 characters) <form:errors path="passwordConfirmed" cssClass="fieldError" />
  		</form:label>
  		<form:password path="password" />
  		<form:label path="confirmPassword">Confirm Password</form:label>
  		<form:password path="confirmPassword" />
  		<input type="hidden" value="${token}" />
	</fieldset>
	<input type="submit" value="Submit">
</form:form>