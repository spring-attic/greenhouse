<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="event" required="true" rtexprvalue="true" type="com.springsource.greenhouse.events.Event" %>
<c:if test="${not empty event.endDate}">
	${event.startDate} through ${event.endDate}
</c:if>
<c:if test="${empty event.endDate}">
	${event.startDate}
	<c:if test="${not empty event.startTime}">
		, ${event.startTime}
	</c:if>
	<c:if test="${not empty event.endTime}">
		 - ${event.endTime}
	</c:if>		
</c:if>