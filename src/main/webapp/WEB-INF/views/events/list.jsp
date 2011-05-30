<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags/dates" prefix="d" %>

<h2>Upcoming Events</h2>

<c:if test="${not empty eventList}">
<dl class="listings">
<c:forEach items="${eventList}" var="event">
	<s:url value="/groups/{group}/events/{year}/{month}/{slug}" var="eventUrl">
		<s:param name="group" value="${event.groupSlug}" />
		<s:param name="year" value="${event.startTime.year}" />
		<s:param name="month" value="${event.startTime.monthOfYear}" />
		<s:param name="slug" value="${event.slug}" />
	</s:url>
	<dt>
		<a href="${eventUrl}"><c:out value="${event.title}" /></a><br/>
	</dt>
	<dd>
		<d:dateRange startTime="${event.startTime}" endTime="${event.endTime}" timeZone="${event.timeZone}" /> at <c:out value="${event.location}" /> <br/>
		<c:out value="${event.description}" escapeXml="true" />		
	</dd>	
</c:forEach>
</dl>
</c:if>