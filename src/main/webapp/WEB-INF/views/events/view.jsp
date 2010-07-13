<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>${event.title}</h2>

<div id="eventInfo">
	<p><strong>When:</strong> ${event.startTime} through ${event.endTime}</p>
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