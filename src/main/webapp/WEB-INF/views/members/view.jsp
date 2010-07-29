<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="personInfo">
	<c:out value="${member.displayName}" escapeXml="true"/>
</div>