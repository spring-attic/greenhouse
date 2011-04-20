<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


	<s:url value="/groups/{group}/events/{year}/{month}/{slug}/tracks" var="actionUrl">
		<s:param name="group" value="${event.groupSlug}" />
		<s:param name="year" value="${event.startTime.year}" />
		<s:param name="month" value="${event.startTime.monthOfYear}" />
		<s:param name="slug" value="${event.slug}" />
	</s:url>
<form:form action="${actionUrl}" method="post" modelAttribute="eventTrackForm">
<fieldset>
	<form:label path="name">Name <form:errors path="name" cssClass="error" /></form:label>
		<form:input path="name" />
	<form:label path="code">Code <form:errors path="code" cssClass="error" /></form:label>
		<form:input path="code" />
		
		<form:label path="description">Description <form:errors path="description" cssClass="error" /></form:label>
		<form:textarea cols="55" rows="8" path="description" />
		
		
		</fieldset>
	<p><button type="submit">Add Track</button></p>	
</form:form>
