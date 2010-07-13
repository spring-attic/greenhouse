<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ attribute name="startDate" required="true" rtexprvalue="true" type="java.util.Date" %>
<%@ attribute name="endDate" required="true" rtexprvalue="true" type="java.util.Date" %>

<c:if test="${startDate.time / 86400000 == endDate.time / 86400000}">
	<fmt:formatDate value="${startDate}" type="date"/>, 
	<fmt:formatDate value="${startDate}" type="time" timeStyle="short"/> - 
	<fmt:formatDate value="${endDate}" type="time" timeStyle="short"/>
</c:if>
<c:if test="${startDate.time / 86400000 != endDate.time / 86400000}">
	<fmt:formatDate value="${startDate}" type="date"/> through <fmt:formatDate value="${endDate}" type="date"/>
</c:if>