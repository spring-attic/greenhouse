<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>${event.title}</h2>

<div id="eventInfo">
	<p>${event.description}</p>	
	
	<h3>What others are tweeting about this event...</h3>
	<ul>
	<c:forEach var="tweet" items="${searchResults.tweets}">	
		<li>${tweet.text}</li>
	</c:forEach>
	</ul>
</div>
