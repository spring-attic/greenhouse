<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ attribute name="startTime" required="true" rtexprvalue="true" type="org.joda.time.DateTime" %>
<%@ attribute name="endTime" required="true" rtexprvalue="true" type="org.joda.time.DateTime" %>

<s:eval var="oneDayEvent" expression="startTime.withZone(jodaTimeContext.timeZone).toLocalDate().equals(endTime.withZone(jodaTimeContext.timeZone).toLocalDate())" />

<c:if test="${oneDayEvent}">
	<joda:format value="${startTime}" style="SS" dateTimeZone="${jodaTimeContext.timeZone}"/> - <joda:format value="${endTime}" style="-S" dateTimeZone="${jodaTimeContext.timeZone}" />
</c:if>
<c:if test="${!oneDayEvent}">
	<joda:format value="${startTime}" style="SS" dateTimeZone="${jodaTimeContext.timeZone}"/> - <joda:format value="${endTime}" style="SS" dateTimeZone="${jodaTimeContext.timeZone}" />
</c:if>