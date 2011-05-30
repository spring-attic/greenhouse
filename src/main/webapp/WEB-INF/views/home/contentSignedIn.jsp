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
		If this is your first time here, start by <a href="<c:url value="/settings"/>">connecting your Greenhouse account</a> to your social networks, then grab the mobile client.
	</p>
</div>
<div id="appIcons">
	<a href="http://itunes.apple.com/us/app/greenhouse/id395862873"><img src="<c:url value="/resources/mobile/icon-apple-appstore.gif" />" /></a>
	<a href="https://market.android.com/details?id=com.springsource.greenhouse"><img src="<c:url value="/resources/mobile/icon-android-marketplace.gif" />" /></a>
</div>