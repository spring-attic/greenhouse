<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

<c:if test="${not empty searchResults.tweets}">
<h3>What others are tweeting about this event...</h3>
<ul class="listings">
<c:forEach var="tweet" items="${searchResults.tweets}">	
	<li class="listing"><strong>${tweet.fromUser} says</strong> ${tweet.text}</li>
</c:forEach>
</ul>
</c:if>
