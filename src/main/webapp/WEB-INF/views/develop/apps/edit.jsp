<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:url var="actionUrl" value="/develop/apps/${slug}" />
<form:form action="${actionUrl}" method="post" modelAttribute="appForm">
	<div class="formInfo">
		<h2>Edit App</h2>
		<s:bind path="*">
		<c:if test="${status.error}">
		<div class="error">Unable to save edits. Please fix the errors below and try again.</div>
		</c:if>
		</s:bind>
 	</div>
	
	<jsp:include page="appFormFragment.jsp" />
	
	<input type="hidden" name="_method" value="PUT" />
	<p><button type="submit">Save</button></p>
</form:form>