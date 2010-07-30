<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:url value="/signup" var="signupUrl" />
<form:form id="signup" action="${signupUrl}" method="post" modelAttribute="signupForm">
	<div class="formInfo">
  		<h2>Sign up</h2>
  		<s:bind path="*">
  		    <c:choose>
	  			<c:when test="${status.error}">
			  		<div class="error">Unable to signup. Please fix the errors below and resubmit.</div>
	  			</c:when>
  			</c:choose>			
  		</s:bind>
  		<p>Please complete the following form to create a Greenhouse account</p>  		
	</div>
  	<fieldset>
  		<form:label path="firstName">
  			First Name <form:errors path="firstName" cssClass="error" />
 		</form:label>
  		<form:input path="firstName" />
  		
  		<form:label path="lastName">
  			Last Name <form:errors path="lastName" cssClass="error" />
 		</form:label>
  		<form:input path="lastName" />
  		
  		<form:label path="email">
  			Email <form:errors path="email" cssClass="error" />
  		</form:label>
  		<form:input path="email" />
  		
  		<form:label path="password">
  			Password (at least 6 characters) <form:errors path="passwordConfirmed" cssClass="error" />
  		</form:label>
  		<form:password path="password" />

  		<form:label path="confirmPassword">Confirm Password</form:label>
  		<form:password path="confirmPassword" />
	</fieldset>
	<input type="submit" value="Sign up">
</form:form>