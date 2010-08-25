<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="groupInfo">
	<p>
		<c:out value="${group.name}" />
	</p>
	<p>
		<c:out value="${group.description}" escapeXml="true" />
	</p>

	<div id="fb-root"></div>
	<fb:like></fb:like>
</div>