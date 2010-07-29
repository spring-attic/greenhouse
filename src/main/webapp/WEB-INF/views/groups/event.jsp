<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags/dates" prefix="d" %>

<h2><c:out value="${event.title}"/></h2>

<div id="eventInfo">
	<p>
		<strong>When:</strong> <d:dateRange startTime="${event.startTime}" endTime="${event.endTime}" />
	</p>
	<p>
		<strong>Where:</strong> <c:out value="${event.location}" escapeXml="true" />
	</p>
	<p>
		<c:out value="${event.description}" escapeXml="true" />
	</p>		
</div>

<c:if test="${not empty searchResults.tweets}">
	<div id="eventTweets">
		<h3>What others are tweeting about this event...</h3>
		<ul class="eventTweets">
		<c:forEach var="tweet" items="${searchResults.tweets}">	
			<li class="eventTweet"><strong>${tweet.fromUser} says</strong> ${tweet.text}</li>
		</c:forEach>
		</ul>
	</div>
</c:if>