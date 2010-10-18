<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="intro">
	<h2>Welcome to the Greenhouse!</h2>
	<p>
		We make it fun to be an application developer.
	</p>
	<p>
		We help you connect with fellow developers and take advantage of everything the Spring community has to offer.	
	</p>
	<p>
		If this is your first time here, start by <a href="<c:url value="/settings"/>">connecting your account</a> to your social networks, then grab the Greenhouse mobile client.
		You may also use the navigation links at the top of the page to explore additional functionality.
	</p>
</div>
<div id="appIcons">
	<a href="<c:url value="/iphone" />"><img src="<c:url value="/resources/mobile/icon-apple-appstore.gif"/>" /></a>	
	<a href="<c:url value="/android" />"><img src="<c:url value="/resources/mobile/icon-android-marketplace.gif"/>" /></a>	
</div>