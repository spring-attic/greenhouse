<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:url var="actionUrl" value="/develop/apps" />
<form:form action="${actionUrl}" method="post" modelAttribute="appForm">
	<div class="formInfo">
		<h2>Register App</h2>
		<s:bind path="*">
		<c:if test="${status.error}">
		<div class="error">Unable to register. Please fix the errors below and resubmit.</div>
		</c:if>
		</s:bind>
		<p>Connect one of your apps to Greenhouse by completing the following form.</p>  		
	</div>
	
	<jsp:include page="appFormFragment.jsp" />
	
	<p><button type="submit">Register</button></p>
</form:form>