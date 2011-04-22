<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>


<fieldset>
<form:label path="title">Title <form:errors path="title" cssClass="error" /></form:label>
		<form:input path="title" />
		

		<form:label path="startDate">Start Date <form:errors path="startDate" cssClass="error" /><form:errors path="startHour" cssClass="error" /></form:label>
		<div class="multiple">
		<table>
		<tr>
		<td>
		<form:input path="startDate" />
		</td>
		<td VALIGN="top">
		
			<form:select path="startHour">
				<form:option value="">Hour</form:option>
				<form:option value="1">1</form:option>
				<form:option value="2">2</form:option>
				<form:option value="3">3</form:option>
				<form:option value="4">4</form:option>
				<form:option value="5">5</form:option>
				<form:option value="6">6</form:option>
				<form:option value="7">7</form:option>
				<form:option value="8">8</form:option>
				<form:option value="9">9</form:option>
				<form:option value="10">10</form:option>
				<form:option value="11">11</form:option>
				<form:option value="12">12</form:option>
			</form:select>
			
			<form:select path="startMinute">
				<form:option value="">Minute</form:option>		
				<form:option value="00" />
				<form:option value="15" />
				<form:option value="30" />
				<form:option value="45" />
			</form:select>
			
			<form:select path="startAmPm">
				<form:option value="">AM/PM</form:option>
				<form:option value="AM" />
				<form:option value="PM" />
			</form:select>
			
		</td>
		</tr>
		
		</table>
		</div>
		<form:label path="endDate">End Date <form:errors path="endDate" cssClass="error" /></form:label>
		<div class="multiple">
		<table>
		<tr>
		<td>
		<form:input path="endDate" />
		</td>
		<td VALIGN="top">
		
			<form:select path="endHour">
			<form:option value="">Hour</form:option>
				<form:option value="1">1</form:option>
				<form:option value="2">2</form:option>
				<form:option value="3">3</form:option>
				<form:option value="4">4</form:option>
				<form:option value="5">5</form:option>
				<form:option value="6">6</form:option>
				<form:option value="7">7</form:option>
				<form:option value="8">8</form:option>
				<form:option value="9">9</form:option>
				<form:option value="10">10</form:option>
				<form:option value="11">11</form:option>
				<form:option value="12">12</form:option>
			</form:select>
			<form:select path="endMinute">
				<form:option value="">Minute</form:option>
				<form:option value="00" />	
				<form:option value="15" />
				<form:option value="30" />
				<form:option value="45" />
			</form:select>
			<form:select path="endAmPm">
				<form:option value="">AM/PM</form:option>
				<form:option value="AM" />
				<form:option value="PM" />
			</form:select>
		</td>
		</tr>
		
		</table>
		</div>
		<form:label path="description">Description <form:errors path="description" cssClass="error" /></form:label>
		<form:textarea cols="55" rows="8" path="description" />
		
			<jsp:include page="sessionFragment.jsp" />
		</fieldset>
		
		
		<script type="text/javascript">
		
		$(document).ready(function() {
			var name = new String;
			$("#startDate").datepicker({clickInput:true});
			$("#endDate").datepicker({clickInput:true});
		});
		
		</script>
		