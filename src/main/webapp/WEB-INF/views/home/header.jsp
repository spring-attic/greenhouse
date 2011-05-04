<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h1><a title="Greenhouse" href="<c:url value="/" />"><img src="<c:url value="/resources/logo-header.png" />" alt="Greenhouse" /></a></h1>
<div id="nav">
	<ul>
		<li><a href="<c:url value="/signin" />">Sign In</a></li>
		<li><a href="<c:url value="/signup" />">Join Now</a></li>
	</ul>
</div>