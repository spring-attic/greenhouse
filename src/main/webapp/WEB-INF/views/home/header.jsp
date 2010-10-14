<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="topbar">
	Already a member? <a href="<c:url value="/signin" />">Sign In</a>
</div>
<h1><a href="<c:url value="/" />">Greenhouse</a></h1>
<a href="<c:url value="/signup" />">Join Now</a>