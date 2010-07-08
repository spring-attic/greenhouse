<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<form:form id="signup" method="post" modelAttribute="signupForm">
	<div class="header">
  		<h2>Sign up</h2>
  		<s:bind path="*">
  			<c:if test="${status.error}">
		  		<div class="error">Unable to sign up.  Please complete all fields.</div>
  			</c:if>
  			<c:if test="${!status.error}">
		  		<div class="info">Please complete the following form to create a Greenhouse account</div>
  			</c:if>  			
  		</s:bind>
	</div>
  	<fieldset>
  		<form:label path="firstName">
  			First Name <form:errors path="firstName" cssClass="fieldError" />
 		</form:label>
  		<form:input path="firstName" />
  		
  		<form:label path="lastName">
  			Last Name <form:errors path="lastName" cssClass="fieldError" />
 		</form:label>
  		<form:input path="lastName" />
  		
  		<form:label path="email">
  			Email <form:errors path="email" cssClass="fieldError" />
  		</form:label>
  		<form:input path="email" />
  		
  		<form:label path="password">
  			Password (at least 6 characters) <form:errors path="passwordConfirmed" cssClass="fieldError" />
  		</form:label>
  		<form:password path="password" />

  		<form:label path="confirmPassword">Confirm Password</form:label>
  		<form:password path="confirmPassword" />
	</fieldset>
	<input type="submit" value="Sign up">
</form:form>