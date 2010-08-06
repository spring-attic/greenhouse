<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="groupInfo">
	<p><c:out value="${group.name}" escapeXml="true"/></p>
	<p><c:out value="${group.description}" escapeXml="true"/></p>
</div>

<fb:like></fb:like>