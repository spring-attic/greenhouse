<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>


<fieldset>
<form:label path="title">Title <form:errors path="title" cssClass="error" /></form:label>
		<form:input path="title" />
		
		<form:label path="hashtag">Twitter Hashtag <form:errors path="hashtag" cssclass="error"/></form:label>
		<form:input path="hashtag"/>

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
		
		<form:label path="trackCode">Assign to Track <form:errors path="trackCode" cssClass="error" /></form:label>
		<form:select path="trackCode">
			<form:option value="">Track</form:option>
				<c:forEach var="track" items="${trackList}" varStatus="status">
			<form:option value="${track.code}">${track.name}</form:option>
				</c:forEach>
		</form:select> 
		<form:label path="leaderID">Select Speaker </form:label>
		<form:select path="leaderID" onchange='onChange(this.form.leaderID);'>
			<form:option value="">Add Leader</form:option>
				<c:forEach var="speaker" items="${speakerList}" varStatus="status">
			<form:option value="${status.index+1}">${speaker}</form:option>
				</c:forEach>
		</form:select> 
		<form:label path="name">Leader Name <form:errors path="name" cssClass="error"/></form:label>
		<form:input path="name"/>
		<form:label path="company">Company <form:errors path="company" cssClass="error"/></form:label>
		<form:input path="company"/>
		<form:label path="companyTitle">Title <form:errors path="companyTitle" cssClass="error"/></form:label>
		<form:input path="companyTitle"/>
		<form:label path="companyURL">Company URL <form:errors path="companyURL" cssClass="error"/></form:label>
		<form:input path="companyURL"/>
		<form:label path="twitterName">Twitter Username <form:errors path="twitterName" cssClass="error"/></form:label>
		<form:input path="twitterName"/>
		


		<p><button type="submit">Add New Session</button></p>
		</fieldset>
		
		
		<script type="text/javascript">
		
		$(document).ready(function() {
			var name = new String;
			$("#startDate").datepicker({clickInput:true});
			$("#endDate").datepicker({clickInput:true});
		});
		
		function onChange(dropdown){
			var index = dropdown.selectedIndex;
			if (index != 0) {
					$("#name").attr("disabled",true);
					$("#company").attr("disabled", true);
					$("#companyTitle").attr("disabled", true);
					$("#companyURL").attr("disabled" , true);
					$("#twitterName").attr("disabled", true);
					} else {
						$("#name").attr("disabled", false);
						$("#company").attr("disabled", false);
						$("#companyTitle").attr("disabled", false);
						$("#companyURL").attr("disabled" , false);
						$("#twitterName").attr("disabled", false);
					}}
		
		</script>
		