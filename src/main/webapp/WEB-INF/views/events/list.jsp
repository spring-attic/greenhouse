<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<h2>Upcoming Events</h2>

<c:if test="${not empty eventList}">
	<dl>
	<c:forEach items="${eventList}" var="event">
		<s:url value="/events/{id}" var="eventUrl">
			<s:param name="id" value="${event.id}" />
		</s:url> 
		<dt class="event"><a href="${eventUrl}">${event.title}</a></dt>
		<dd class="event"><c:out value="${event.description}"/></dd>
	</c:forEach>
	</dl>
</c:if>
