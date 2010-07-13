<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h2>${event.title}</h2>

<div id="eventInfo">
	<p><strong>When:</strong>
		<c:if test="${event.startTime.time / 86400000 == event.endTime.time / 86400000}">
			<fmt:formatDate value="${event.startTime}" type="date"/>, 
			<fmt:formatDate value="${event.startTime}" type="time" timeStyle="short"/> - 
			<fmt:formatDate value="${event.endTime}" type="time" timeStyle="short"/>
		</c:if>
		<c:if test="${event.startTime.time / 86400000 != event.endTime.time / 86400000}">
			<fmt:formatDate value="${event.startTime}" type="date"/> through <fmt:formatDate value="${event.endTime}" type="date"/>
		</c:if>
	</p>
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