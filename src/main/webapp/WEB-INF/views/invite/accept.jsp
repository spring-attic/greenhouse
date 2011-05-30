<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:url value="/invite" var="inviteUrl" />
<!-- TODO use Web Flow here so invitee and sentBy state can be preserved through the accept flow -->
<c:if test="${invitee != null}">
<h2>Welcome to the Greenhouse, ${invitee.firstName}!</h2>
</c:if>

<c:url value="/invite/accept" var="acceptUrl" />
<form:form action="${acceptUrl}" method="post" modelAttribute="signupForm">
	<div class="formInfo">
		<h2>Accept Invite</h2>
  		<s:bind path="*">
  		<c:choose>
  		<c:when test="${status.error}">
  			<div class="error">Unable to sign up. Please fix the errors below and resubmit.</div>
  		</c:when>
  		</c:choose>			
  		</s:bind>
		<p>
			<c:if test="${sentBy != null}">
			You've been invited to the Greenhouse by <a href="<c:url value="/members/${sentBy.id}" />">${sentBy.label}</a>.
			</c:if>
			Accept your invitation by completing the signup form below.
		</p>
	</div>
	<%@ include file="../signup/signupForm.jspf" %>
	<input type="hidden" name="token" value="${token}" />
	<p><button type="submit">Accept Invite and Sign Up</button></p>
</form:form>
