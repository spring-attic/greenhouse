<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="value" required="true" rtexprvalue="true" type="com.springsource.greenhouse.develop.AppSummary" %>
<div class="listing">
	<img src="${value.iconUrl}" alt="App Icon" />
	<h3><a href="<c:url value="/develop/apps/${value.slug}" />"><c:out value="${value.name}" /></a></h3>
	<p><c:out value="${value.description}" /></p>
</div>
