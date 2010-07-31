<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ attribute name="startTime" required="true" rtexprvalue="true" type="org.joda.time.LocalDateTime" %>
<%@ attribute name="endTime" required="true" rtexprvalue="true" type="org.joda.time.LocalDateTime" %>

<s:eval var="oneDayEvent" expression="startTime.toLocalDate().equals(endTime.toLocalDate())" />

<c:if test="${oneDayEvent}">
	<joda:format value="${startTime}" style="SS" dateTimeZone="${jodaTimeContext.timeZone}"/> - <joda:format value="${endTime}" style="-S" dateTimeZone="${jodaTimeContext.timeZone}" />
</c:if>
<c:if test="${!oneDayEvent}">
	<joda:format value="${startTime}" style="S-" /> through <joda:format value="${endTime}" style="S-" />
</c:if>