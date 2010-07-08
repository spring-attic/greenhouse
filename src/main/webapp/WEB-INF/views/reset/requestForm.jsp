<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<form:form id="resetPassword" method="post" modelAttribute="resetRequestForm">
	<div class="header">
  		<h2>Sign up</h2>
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
  		<form:label path="username">
  			Your username or password <form:errors path="username" cssClass="fieldError" />
  		</form:label>
  		<form:password path="username" />
	</fieldset>
	<input type="submit" value="Request a Password Reset">
</form:form>
