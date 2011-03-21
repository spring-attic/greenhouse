<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>

	<fieldset>
		<form:label path="title">Title <form:errors path="title" cssClass="error" /></form:label>
		<form:input path="title" />
		
		<form:label path="tz">Time Zone</form:label>
			<form:select path="tz">
			<form:option value="">Select One</form:option>
			<c:forEach var="zones" items="${timezoneList}">
			<form:option value="${zones}">${zones}</form:option>
			</c:forEach>
		</form:select> 
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
				<form:option value="0" />
				<form:option value="1" />
				<form:option value="2" />
				<form:option value="3" />
				<form:option value="4" />
				<form:option value="5" />
				<form:option value="6" />
				<form:option value="7" />
				<form:option value="8" />
				<form:option value="9" />
				<form:option value="10" />
				<form:option value="11" />
				<form:option value="12" />
				<form:option value="13" />
				<form:option value="14" />
				<form:option value="15" />
				<form:option value="16" />
				<form:option value="17" />
				<form:option value="18" />
				<form:option value="19" />
				<form:option value="20" />
				<form:option value="21" />
				<form:option value="22" />
				<form:option value="23" />
				<form:option value="24" />
				<form:option value="25" />
				<form:option value="26" />
				<form:option value="27" />
				<form:option value="28" />
				<form:option value="29" />
				<form:option value="30" />
				<form:option value="31" />
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
				<form:option value="0" />	
				<form:option value="1" />
				<form:option value="2" />
				<form:option value="3" />
				<form:option value="4" />
				<form:option value="5" />
				<form:option value="6" />
				<form:option value="7" />
				<form:option value="8" />
				<form:option value="9" />
				<form:option value="10" />
				<form:option value="11" />
				<form:option value="12" />
				<form:option value="13" />
				<form:option value="14" />
				<form:option value="15" />
				<form:option value="16" />
				<form:option value="17" />
				<form:option value="18" />
				<form:option value="19" />
				<form:option value="20" />
				<form:option value="21" />
				<form:option value="22" />
				<form:option value="23" />
				<form:option value="24" />
				<form:option value="25" />
				<form:option value="26" />
				<form:option value="27" />
				<form:option value="28" />
				<form:option value="29" />
				<form:option value="30" />
				<form:option value="31" />
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
		<form:label path="description">Description</form:label>
		<form:textarea cols="55" rows="8" path="description" />
	</fieldset>
	
	
		<script type="text/javascript">
	
		$(document).ready(function() {
			$("#startDate").datepicker({clickInput:true});
			$("#endDate").datepicker({clickInput:true});
		});
	
	</script>
	
	