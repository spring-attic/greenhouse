<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

	<fieldset>
		<form:label path="title">Title <form:errors path="title" cssClass="error" /></form:label>
		<form:input path="title" />
		<label>Time Zone</label>
		<select>
			<option value="">Select One</option>
				<option value="1">Pacific</option>
				<option value="2">Mountain</option>
				<option value="3">Central</option>
				<option value="4">Eastern</option>
			</select>
				
		<form:label path="startTime">Start Date<form:errors path="startTime" cssClass="error" /></form:label>
		<input id="starttime" type="text">
		<form:label path="endTime">End Date<form:errors path="endTime" cssClass="error" /></form:label>
		<input id="endtime" type="text">
		
		<form:label path="description">Description</form:label>
		<form:textarea cols="55" rows="8" path="description" />
	</fieldset>
	
	<script type="text/javascript">
	
		$(document).ready(function() {
			$("#starttime").datepicker({clickInput:true});
			$("#endtime").datepicker({clickInput:true});
		});
	
	</script>