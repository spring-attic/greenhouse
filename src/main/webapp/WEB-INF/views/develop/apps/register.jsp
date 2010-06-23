<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<form:form id="register" action="<c:url value="/develop/apps/register" />" method="post" modelAttribute="appRegistrationForm">
  	<fieldset>
  		<legend>Register Application</legend>
  		
  		<form:label path="name">Application Name</form:label>
  		<form:input path="name" />
  		
  		<form:label path="description">Description</form:label>
  		<form:input path="description" />

  		<form:label path="website">Website</form:label>
  		<form:input path="website" />
  		
  		<form:label path="organization">Organization</form:label>
  		<form:input path="organization" />
  		
  		<form:label path="callbackUrl">Callback URL</form:label>
  		<form:input path="callbackUrl" />
  		
		<input type="submit" value="Register"/>
	</fieldset>
</form:form>