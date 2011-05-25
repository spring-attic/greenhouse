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
</dl>
<p>
	Grab the Greenhouse mobile client to follow this Event as it happens.
</p>
<div id="appIcons" style="padding: 0 0 20px 0 ;">
	<a href="http://itunes.apple.com/us/app/greenhouse/id395862873"><img src="<c:url value="/resources/mobile/icon-apple-appstore.gif" />" /></a>
	<a href="https://market.android.com/details?id=com.springsource.greenhouse"><img src="<c:url value="/resources/mobile/icon-android-marketplace.gif" />" /></a>
</div>