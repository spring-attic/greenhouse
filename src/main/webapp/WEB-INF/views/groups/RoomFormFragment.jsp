<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>

	<div class="formInfo">
		<h2>Add Room</h2>
		<s:bind path="*">
		<c:if test="${status.error}">
		<div class="error">Unable to add a room. Please fix the errors below and resubmit.</div>
		</c:if>
		</s:bind>
		<p>Add a Room to a Venue.</p>  		
	</div>

<fieldset>

<form:label path="name">Name <form:errors path="name" cssClass="error" /></form:label>
		<form:input path="name" />
		
<form:label path ="capacity">Capacity <form:errors path="capacity" cssClass="error" /></form:label>
		<form:input path="capacity" />
		
<form:label path ="locationHint">Location Hint <form:errors path="locationHint" cssClass="error" /></form:label>
		<form:input path="locationHint" />
		
<p><button type="submit">Add a Room</button></p>
			
</fieldset>