<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags/dates" prefix="d" %>

<h2><c:out value="${event.title}"/></h2>

<dl>
	<dt>When</dt>
	<dd><d:dateRange startTime="${event.startTime}" endTime="${event.endTime}" timeZone="${event.timeZone}" /></dd>
	<dt>Where</dt>
	<dd><c:out value="${event.location}" /></dd>
	<dt>Description</dt>
	<dd><c:out value="${event.description}" escapeXml="true" /></dd>
	<dt>Sessions:</dt>
	<dd>
	<c:forEach var="sessions" items="${sessionList}">
	<s:url value="/groups/{group}/events/{year}/{month}/{slug}/sessions/{sessionid}" var="sessionEditUrl">
		<s:param name="group" value="${event.groupSlug}" />
		<s:param name="year" value="${event.startTime.year}" />
		<s:param name="month" value="${event.startTime.monthOfYear}" />
		<s:param name="slug" value="${event.slug}" />
		<s:param name="sessionid" value="${sessions.id}" />
	</s:url>
	<div><a href="${sessionEditUrl}"><c:out value="${sessions.title}" escapeXml="true" /></a></div>
	</c:forEach>
	</dd>
	<dt>Tracks:</dt>
	<dd>
	<c:forEach var="tracks" items="${trackList}">
	<s:url value="/groups/{group}/events/{year}/{month}/{slug}/tracks/{trackcode}" var="trackViewUrl">
		<s:param name="group" value="${event.groupSlug}" />
		<s:param name="year" value="${event.startTime.year}" />
		<s:param name="month" value="${event.startTime.monthOfYear}" />
		<s:param name="slug" value="${event.slug}" />
		<s:param name="trackcode" value="${tracks.code}" />
	</s:url>
	<div><a href="${trackViewUrl}"><c:out value="${tracks.name}" escapeXml="true" /></a></div>
	</c:forEach>
	</dd>
	<s:url value="/groups/{group}/events/{year}/{month}/{slug}/tracks/new" var="tracksUrl">
		<s:param name="group" value="${event.groupSlug}" />
		<s:param name="year" value="${event.startTime.year}" />
		<s:param name="month" value="${event.startTime.monthOfYear}" />
		<s:param name="slug" value="${event.slug}" />
	</s:url>
	<s:url value="/groups/{group}/events/{year}/{month}/{slug}/sessions/new" var="sessionsUrl">
		<s:param name="group" value="${event.groupSlug}" />
		<s:param name="year" value="${event.startTime.year}" />
		<s:param name="month" value="${event.startTime.monthOfYear}" />
		<s:param name="slug" value="${event.slug}" />
	</s:url>
	<h4 align="center">
		<a href="${tracksUrl}">Add a Track</a><br/>
		<a href="${sessionsUrl}">Add a Session</a>
	</h4>
</dl>
<p>
	Grab the Greenhouse mobile client to follow this Event as it happens.
</p>
<div id="appIcons">
	<a href="http://itunes.apple.com/us/app/greenhouse/id395862873"><img src="<c:url value="/resources/mobile/icon-apple-appstore.gif" />" /></a>	
</div>