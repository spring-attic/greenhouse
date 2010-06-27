<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<form:form id="signup" cssClass="cleanform" method="post" modelAttribute="signupForm">
	<div class="formInfo">
  		<h3>Sign up</h3>
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
  		<form:label path="firstName">First Name</form:label>
  		<form:input path="firstName" />
  		
  		<form:label path="lastName">Last Name</form:label>
  		<form:input path="lastName" />
  		
  		<form:label path="email">Email</form:label>
  		<form:input path="email" />
  		
  		<form:label path="password">Password (at least 6 characters)</form:label>
  		<form:password path="password" />
  		
  		<form:label path="confirmPassword">Confirm Password</form:label>
  		<form:password path="confirmPassword" />	
	</fieldset>
	<input type="submit" value="Sign up">
		
</form:form>