<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>

<form:label path="venueID">Select Venue</form:label>
		<form:select path="venueID">
				<form:option value="">Add New Venue</form:option>
					<c:forEach var="venues" items="${venueList}">
				<form:option value="${venues}">${venues}</form:option>
					</c:forEach>
		</form:select> 
	
		<form:label path="venueName">Venue Name <form:errors path="venueName" cssClass="error" /></form:label>
		<form:input path="venueName" />
		<form:label path="venueAddress">Venue Address <form:errors path="venueAddress" cssClass="error" /></form:label>
		<form:input path="venueAddress" />
		<form:label path="locationHint">Location Hint(optional) <form:errors path="locationHint" cssClass="error" /></form:label>
		<form:input path="locationHint" />
		