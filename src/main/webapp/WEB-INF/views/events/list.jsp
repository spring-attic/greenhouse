<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags/dates" prefix="d" %>

<h2>Upcoming Events</h2>

<c:if test="${not empty eventList}">
	<dl>
	<c:forEach items="${eventList}" var="event">
		<s:url value="/groups/{group}/events/{year}/{month}" var="eventUrl">
			<s:param name="group" value="${event.group}" />
			<s:param name="year" value="${event.startDate.year}" />
			<s:param name="month" value="${event.startDate.month}" />
		</s:url>
		<dt class="event">
			<a href="${eventUrl}">${event.title}</a> <br/>
			<span class="locationAndDate">
				<d:displayDateRange startDate="${event.startDate}" endDate="${event.endDate}"/> - ${event.location}
			</span>
		</dt>
		<dd class="event">
			<c:out value="${event.description}" />
		</dd>
	</c:forEach>
	</dl>
</c:if>
