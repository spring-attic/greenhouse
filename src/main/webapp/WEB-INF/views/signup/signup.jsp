<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:if test="${not empty message}">
<div class="${message.type.cssClass}">${message.text}</div>
</c:if>

<c:url value="/signup" var="signupUrl" />
<form:form id="signup" action="${signupUrl}" method="post" modelAttribute="signupForm">
	<div class="formInfo">
  		<h2>Sign Up at the Greenhouse</h2>
  		<s:bind path="*">
  		<c:choose>
  		<c:when test="${status.error}">
  			<div class="error">Unable to sign up. Please fix the errors below and resubmit.</div>
  		</c:when>
  		</c:choose>			
  		</s:bind>
  		<p>Join Greenhouse to connect with other application developers using Spring.</p>  		
	</div>
	<%@ include file="signupForm.jspf" %>
	<p><button type="submit">Sign Up</button></p>
</form:form>