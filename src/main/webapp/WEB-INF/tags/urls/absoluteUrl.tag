<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="value" required="true" rtexprvalue="true" type="java.lang.String" %>
<c:set var="scheme" value="${pageContext.request.scheme}"/>
<c:set var="serverPort" value="${pageContext.request.serverPort}"/>
<c:choose>
	<c:when test="${(scheme eq 'http' and serverPort eq '80') or (scheme eq 'https' and serverPort eq '443')}">
		<c:set var="port" value=""/>
	</c:when>
	<c:otherwise>
		<c:set var="port" value=":${serverPort}"/>
	</c:otherwise>	
</c:choose>
${scheme}://${pageContext.request.serverName}${port}<s:url value="${value}" />