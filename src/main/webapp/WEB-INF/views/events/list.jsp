<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<h2>Events</h2>

<c:if test="${not empty eventList}">
	<c:forEach items="${eventList}" var="event">
		<s:url value="/events/{id}" var="eventUrl">
			<s:param name="id" value="${event.id}" />
		</s:url> 
		<li><a href="${eventUrl}">${event.title}</a></li>
	</c:forEach>
</c:if>
