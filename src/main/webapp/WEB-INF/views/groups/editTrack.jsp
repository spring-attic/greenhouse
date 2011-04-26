<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

	<s:url value="/groups/{group}/events/{year}/{month}/{slug}/tracks/{trackcode}" var="actionUrl">
		<s:param name="group" value="${event.groupSlug}" />
		<s:param name="year" value="${event.startTime.year}" />
		<s:param name="month" value="${event.startTime.monthOfYear}" />
		<s:param name="slug" value="${event.slug}" />
		<s:param name="trackcode" value="${trackcode}" />
	</s:url>
<form:form action="${actionUrl}" method="post" modelAttribute="eventTrackForm">
	<div class="formInfo">
		<h2>Edit Track</h2>
		<s:bind path="*">
		<c:if test="${status.error}">
		<div class="error">Unable to save edits. Please fix the errors below and try again.</div>
		</c:if>
		</s:bind>
 	</div>
	
	<fieldset>
	<form:label path="name">Track Name <form:errors path="name" cssClass="error" /></form:label>
		<form:input path="name" />
	<form:label path="code">Track Code <form:errors path="code" cssClass="error" /></form:label>
		<form:input path="code" />
		
		<form:label path="description">Track Description <form:errors path="description" cssClass="error" /></form:label>
		<form:textarea cols="55" rows="8" path="description" />
		
		
	</fieldset>
	
	<input type="hidden" name="_method" value="PUT" />
	<p><button type="submit">Save</button></p>
</form:form>