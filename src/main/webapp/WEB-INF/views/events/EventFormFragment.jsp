<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

	<fieldset>
		<form:label path="title">Title <form:errors path="title" cssClass="error" /></form:label>
		<form:input path="title" />
		<label>Time Zone</label>
		<form:select path="timezone">
			<form:option value="">Select One</form:option>
				<form:option value="America/Chicago">America/Chicago</form:option>
				<form:option value="Mountain">Mountain</form:option>
				<form:option value="Central">Central</form:option>
				<form:option value="Easter">Eastern</form:option>
			</form:select>

		<form:label path="startTime">Start Date<form:errors path="startTime" cssClass="error" /></form:label>
		<form:input path="startTime" />
		<form:label path="endTime">End Date<form:errors path="endTime" cssClass="error" /></form:label>
		<form:input path="endTime" />
		
		<form:label path="description">Description</form:label>
		<form:textarea cols="55" rows="8" path="description" />
	</fieldset>
	
	<script type="text/javascript">
	
		$(document).ready(function() {
			$("#startTime").datepicker({clickInput:true});
			$("#endTime").datepicker({clickInput:true});
		});
	
	</script>