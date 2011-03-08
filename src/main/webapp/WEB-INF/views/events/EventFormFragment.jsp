<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

	<fieldset>
		<form:label path="title">Title <form:errors path="title" cssClass="error" /></form:label>
		<form:input path="title" />
		<label>Time Zone</label>
		<select>
			<option value="">Select One</option>
				<option value="America/Chicago">America/Chicago</option>
				<option value="Mountain">Mountain</option>
				<option value="Central">Central</option>
				<option value="Easter">Eastern</option>
			</select>

		<form:label path="startTime">Start Date<form:errors path="startTime" cssClass="error" /></form:label>
		<form:input path="startTime" />
		<form:label path="endTime">End Date<form:errors path="endTime" cssClass="error" /></form:label>
		<form:input path="endTime" />
		
		<form:label path="description">Description</form:label>
		<form:textarea cols="55" rows="8" path="description" />
	</fieldset>
	
