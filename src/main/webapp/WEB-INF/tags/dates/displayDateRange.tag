<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ attribute name="startDate" required="true" rtexprvalue="true" type="org.joda.time.DateTime" %>
<%@ attribute name="endDate" required="true" rtexprvalue="true" type="org.joda.time.DateTime" %>

<c:if test="${startDate.millis / 86400000 == endDate.millis / 86400000}">
	<joda:format value="${startDate}" style="SS"/> - <joda:format value="${endDate}" style="-S" />
</c:if>
<c:if test="${startDate.millis / 86400000 != endDate.millis / 86400000}">
	<joda:format value="${startDate}" style="S-"/> through <joda:format value="${endDate}" style="S-"/>
</c:if>