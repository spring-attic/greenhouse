<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>

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

<facebook:init apiKey="21aa96c8bc23259d0dd2ab99e496c306" />
