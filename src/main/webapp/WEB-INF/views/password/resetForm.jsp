<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<form:form id="resetPassword" method="post" modelAttribute="resetPasswordForm">
	<div class="header">
  		<h2>Reset password</h2>
  		<s:bind path="*">
  			<c:if test="${status.error}">
		  		<div class="error">Unable to reset password.  Please complete all fields.</div>
  			</c:if>
  			<c:if test="${!status.error}">
		  		<div class="info">Please complete the following form to reset your Greenhouse password</div>
  			</c:if>  			
  		</s:bind>
	</div>
  	<fieldset>
  		<form:label path="password">
  			Password (at least 6 characters) <form:errors path="passwordConfirmed" cssClass="fieldError" />
  		</form:label>
  		<form:password path="password" />

  		<form:label path="confirmPassword">Confirm Password</form:label>
  		<form:password path="confirmPassword" />
	</fieldset>
	<input type="submit" value="Reset Password">
</form:form>