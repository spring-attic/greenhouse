<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags/dates" prefix="d" %>

<h2><c:out value="${session.title}"/></h2>

<dl>
	<dt>When</dt>
	<dd><d:dateRange startTime="${session.startTime}" endTime="${session.endTime}" timeZone="${event.timeZone}" /></dd>
	<dt>Room</dt>
	<dd><c:out value="${roomName}"  /></dd>
	<dt>Hashtag</dt>
	<dd><c:out value="${session.hashtag}"  /></dd>
	<dt>Description</dt>
	<dd><c:out value="${session.description}"  /></dd>
</dl>
