<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:url var="actionUrl" value="/events"/>
<form:form action="${actionUrl}" method="post" modelAttribute="eventForm">
	<div class="formInfo">
		<h2>Create Event</h2>
		<s:bind path="*">
		<c:if test="${status.error}">
		<div class="error">Unable to create an Event. Please fix the errors below and resubmit.</div>
		</c:if>
		</s:bind>
		<p>Create an Event to display on Greenhouse.</p>  		
	</div>
	
	<jsp:include page="EventFormFragment.jsp" />
	
	<p><button type="submit">Create Event</button></p>
</form:form>