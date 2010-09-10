<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="value" required="true" rtexprvalue="true" type="com.springsource.greenhouse.develop.AppSummary" %>
<div id="appSummary">
	<img src="${value.iconUrl}" alt="Icon" />
	<h3><a href="<c:url value="/develop/apps/${value.slug}" />">${value.name}</h3></a>
	<p>
		<c:out value="${value.description}" />
	</p>
</div>