<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags/dates" prefix="d" %>

<h2>Upcoming Events</h2>

<c:if test="${not empty eventList}">
	<dl>
	<c:forEach items="${eventList}" var="event">
		<s:url value="/events/{event}" var="eventUrl">
			<s:param name="event" value="${event.friendlyId}" />
		</s:url> 
		<dt class="event"><a href="${eventUrl}">${event.title}</a><br/>
			<span class="locationAndDate"><d:displayDateRange startDate="${event.startTime}" endDate="${event.endTime}"/> - 
			${event.location}</span></dt>
		<dd class="event"><c:out value="${event.description}"/></dd>
	</c:forEach>
	</dl>
</c:if>
