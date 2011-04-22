<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>

<form:label path="leaderID">Select Speaker <form:errors path="leaderID" cssClass="error" /></form:label>
		<form:select path="leaderID">
				<form:option value="">Leader</form:option>
					<c:forEach var="speaker" items="${speakerList}" varStatus="status">
				<form:option value="${status.index+1}">${speaker}</form:option>
					</c:forEach>
		</form:select> 

<p><button type="submit">Add New Session</button></p>
