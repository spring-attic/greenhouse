<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ attribute name="startTime" required="true" rtexprvalue="true" type="org.joda.time.DateTime" %>
<%@ attribute name="endTime" required="true" rtexprvalue="true" type="org.joda.time.DateTime" %>
<%@ attribute name="timeZone" required="false" rtexprvalue="true" type="org.joda.time.DateTimeZone" %>

<s:eval var="zone" expression="timeZone != null ? timeZone : jodaTimeContext.timeZone" />
<s:eval var="oneDayEvent" expression="startTime.withZone(zone).toLocalDate().equals(endTime.withZone(zone).toLocalDate())" />

<c:if test="${oneDayEvent}">
	<joda:format value="${startTime}" style="SS" dateTimeZone="${zone}"/> - <joda:format value="${endTime}" style="SS" dateTimeZone="${zone}" /> <s:eval expression="zone.getShortName(endTime)" /> 
</c:if>
<c:if test="${!oneDayEvent}">
	<joda:format value="${startTime}" style="SS" dateTimeZone="${zone}"/> - <joda:format value="${endTime}" style="SS" dateTimeZone="${zone}" /> <s:eval expression="zone.getShortName(endTime)" />
</c:if>