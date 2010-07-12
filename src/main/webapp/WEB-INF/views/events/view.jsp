<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/dates" prefix="d" %>

<h2>${event.title}</h2>

<div id="eventInfo">
	<d:range event="${event}" />
	<p>${event.description}</p>		
</div>

<c:if test="${not empty searchResults.tweets}">
	<div id="eventTweets">
		<h3>What others are tweeting about this event...</h3>
		<ul>
		<c:forEach var="tweet" items="${searchResults.tweets}">	
			<li><strong>${tweet.fromUser} says</strong> ${tweet.text}</li>
		</c:forEach>
		</ul>
	</div>
</c:if>