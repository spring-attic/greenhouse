<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags/dates" prefix="d" %>

<h2><c:out value="${track.name}"/></h2>

<dl>
	<dt>Track code</dt>
	<dd><c:out value="${track.code}" /></dd>
	<dt>Description</dt>
	<dd><c:out value="${track.description}"  /></dd>
</dl>

<s:url value="/groups/{group}/events/{year}/{month}/{slug}/tracks/edit/{trackcode}" var="trackEditUrl">
	<s:param name="group" value="${event.groupSlug}" />
	<s:param name="year" value="${event.startTime.year}" />
	<s:param name="month" value="${event.startTime.monthOfYear}" />
	<s:param name="slug" value="${event.slug}" />
	<s:param name="trackcode" value="${track.code}" />
</s:url>
<a href="${trackEditUrl}">Edit details</a>