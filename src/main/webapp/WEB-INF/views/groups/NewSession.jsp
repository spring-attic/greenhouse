<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


<s:url value="/groups/{group}/events/{year}/{month}/{slug}/sessions" var="actionUrl">
<s:param name="group" value="${event.groupSlug}" />
<s:param name="year" value="${event.startTime.year}" />
<s:param name="month" value="${event.startTime.monthOfYear}" />
<s:param name="slug" value="${event.slug}" />
</s:url>

<form:form action="${actionUrl}" method="post" modelAttribute="eventSessionForm">


<jsp:include page="SessionFormFragment.jsp" />
</form:form>
