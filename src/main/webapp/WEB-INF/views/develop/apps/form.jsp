<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:url var="actionUrl" value="/develop/apps" />
<form:form action="${actionUrl}" method="post" modelAttribute="appForm">
	<div class="formInfo">
		<h2>Register App</h2>
		<s:bind path="*">
		<c:if test="${status.error}">
		<div class="error">Unable to register. Please fix the errors below and resubmit.</div>
		</c:if>
		</s:bind>
		<p>Connect one of your apps to Greenhouse by completing the following form.</p>  		
	</div>
	<fieldset>
	 	<legend>App Details</legend>
	
	 	<form:label path="name">Name <form:errors path="name" cssClass="error" /></form:label>
	 	<form:input path="name" />
	 		
	 	<form:label path="description">Description <form:errors path="description" cssClass="error" /></form:label>
	 	<form:input path="description" />
	
	 	<form:label path="organization">Organization</form:label>
	 	<form:input path="organization" />
	
	 	<form:label path="website">Website</form:label>  
	 	<form:input path="website" />
	 		  		
	 	<form:label path="callbackUrl">Callback URL</form:label>
	 	<form:input path="callbackUrl" /> 		
	</fieldset>
	<input type="submit" value="Register" />
</form:form>