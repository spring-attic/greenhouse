<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags/dates" prefix="d" %>

<h2>${event.title}</h2>

<div id="eventInfo">
	<p><strong>When:</strong> <d:displayDateRange startDate="${event.startTime}" endDate="${event.endTime}"/></p>
	<p><strong>Where:</strong> ${event.location}</p>
	<p>${event.description}</p>		
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