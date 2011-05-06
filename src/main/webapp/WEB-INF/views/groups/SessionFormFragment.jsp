<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>

<fieldset>
	<form:label path="title">Title <form:errors path="title" cssClass="error" /></form:label>
	<form:input path="title" />
	<form:label path="hashtag">Twitter Hashtag <form:errors path="hashtag" cssclass="error"/></form:label>
	<form:input path="hashtag"/>
	<form:label path="startTimeFields.day">Start Date <form:errors path="startTimeFields.day" cssClass="error" /></form:label>
	<div class="multiple">
		<table>
		<tr>
		<td>
		<form:input id="startDate" path="startTimeFields.day" />
		</td>
		<td VALIGN="top">		
			<form:select path="startTimeFields.hour">
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
			<form:select path="startTimeFields.minute">
				<form:option value="">Minute</form:option>		
				<form:option value="00" />
				<form:option value="15" />
				<form:option value="30" />
				<form:option value="45" />
			</form:select>			
			<form:select path="startTimeFields.halfDay">
				<form:option value="">AM/PM</form:option>
				<form:option value="AM" />
				<form:option value="PM" />
			</form:select>			
		</td>
		</tr>
		</table>
	</div>
	<form:label path="endTimeFields.day">End Date <form:errors path="endTimeFields.day" cssClass="error" /></form:label>
	<div class="multiple">
		<table>
		<tr>
		<td>
		<form:input id="endDate" path="endTimeFields.day" />
		</td>
		<td VALIGN="top">	
			<form:select path="endTimeFields.hour">
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
			<form:select path="endTimeFields.minute">
				<form:option value="">Minute</form:option>
				<form:option value="00" />	
				<form:option value="15" />
				<form:option value="30" />
				<form:option value="45" />
			</form:select>
			<form:select path="endTimeFields.halfDay">
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
	<form:label path="leaderFields.id">Select Speaker </form:label>
	<form:select id="leaderId" path="leaderFields.id" onchange='onChange(this.form.leaderId);'>
		<form:option value="">Add New Leader</form:option>
		<c:forEach var="speaker" items="${speakerList}" varStatus="status">
		<form:option value="${status.index+1}">${speaker}</form:option>
		</c:forEach>
	</form:select> 
	<form:label path="leaderFields.name">Leader Name <form:errors path="leaderFields.name" cssClass="error"/></form:label>
	<form:input id="leaderName" path="leaderFields.name"/>
	<form:label path="leaderFields.company">Company <form:errors path="leaderFields.company" cssClass="error"/></form:label>
	<form:input id="leaderCompany" path="leaderFields.company"/>
	<form:label path="leaderFields.companyTitle">Title <form:errors path="leaderFields.companyTitle" cssClass="error"/></form:label>
	<form:input id="leaderCompanyTitle" path="leaderFields.companyTitle"/>
	<form:label path="leaderFields.companyURL">Company URL <form:errors path="leaderFields.companyURL" cssClass="error"/></form:label>
	<form:input id="leaderCompanyUrl" path="leaderFields.companyURL"/>
	<form:label path="leaderFields.twitterName">Twitter Username <form:errors path="leaderFields.twitterName" cssClass="error"/></form:label>
	<form:input id="leaderTwitterName" path="leaderFields.twitterName"/>	
</fieldset>

<p><button type="submit">Add New Session</button></p>
		
<script type="text/javascript">

$(document).ready(function() {
	var name = new String;
	$("#startDate").datepicker({clickInput:true});
	$("#endDate").datepicker({clickInput:true});
});
		
function onChange(dropdown){
	var index = dropdown.selectedIndex;
	if (index != 0) {
		$("#leaderName").attr("disabled",true);
		$("#leaderCompany").attr("disabled", true);
		$("#leaderCompanyTitle").attr("disabled", true);
		$("#leaderCompanyURL").attr("disabled" , true);
		$("#leaderTwitterName").attr("disabled", true);
	} else {
		$("#leaderName").attr("disabled", false);
		$("#leaderCompany").attr("disabled", false);
		$("#leaderCompanyTitle").attr("disabled", false);
		$("#leaderCompanyURL").attr("disabled" , false);
		$("#leaderTwitterName").attr("disabled", false);
	}
}
</script>		