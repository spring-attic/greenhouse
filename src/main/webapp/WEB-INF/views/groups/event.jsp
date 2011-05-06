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
	<dt>Tracks</dt>
	<c:if test="${empty trackList}">
		<dd></dd>
	</c:if>
	<c:forEach var="tracks" items="${trackList}">
	<s:url value="/groups/{group}/events/{year}/{month}/{slug}/tracks/{trackcode}" var="trackViewUrl">
		<s:param name="group" value="${event.groupSlug}" />
		<s:param name="year" value="${event.startTime.year}" />
		<s:param name="month" value="${event.startTime.monthOfYear}" />
		<s:param name="slug" value="${event.slug}" />
		<s:param name="trackcode" value="${tracks.code}" />
	</s:url>
	<dd><a href="${trackViewUrl}"><c:out value="${tracks.name}" escapeXml="true" /></a></dd>
	</c:forEach>
</dl>
<p>
	Grab the Greenhouse mobile client to follow this Event as it happens.
</p>
<div id="appIcons" style="padding: 0 0 20px 0 ;">
	<a href="http://itunes.apple.com/us/app/greenhouse/id395862873"><img src="<c:url value="/resources/mobile/icon-apple-appstore.gif" />" /></a>	
</div>
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
<s:url value="/groups/{group}/events/{year}/{month}/{slug}/rooms/new" var="roomsUrl">
	<s:param name="group" value="${event.groupSlug}" />
	<s:param name="year" value="${event.startTime.year}" />
	<s:param name="month" value="${event.startTime.monthOfYear}" />
	<s:param name="slug" value="${event.slug}" />
</s:url>
<ul class="listings">
	<li class="listing"><a href="${tracksUrl}">Add a Track</a><br/></li>
	<li class="listing"><a href="${sessionsUrl}">Add a Session</a></li>	
	<li class="listing"><a href="${roomsUrl}">Add a Room to the Venue</a></li>
</ul>