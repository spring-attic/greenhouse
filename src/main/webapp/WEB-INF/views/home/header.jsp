<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h1><a href="<c:url value="/" />"><img src="<c:url value="/resources/header-logo.png" />" alt="Greenhouse" /></a></h1>
<div id="topbar">
	Already a member? <a href="<c:url value="/signin" />">Sign In</a>
</div>
<a href="<c:url value="/signup" />">Join Now</a>