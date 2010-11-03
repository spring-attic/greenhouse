<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:if test="${not empty message}">
<div class="${message.type.cssClass}">${message.text}</div>
</c:if>

<c:url value="/invite/mail" var="inviteUrl" />
<form:form action="${inviteUrl}" method="post" modelAttribute="mailInviteForm">
	<div class="formInfo">
  		<h2>Email Invites</h2>
  		<!-- TODO clean this up -->
  		<s:bind path="*">
		<c:if test="${status.error}">
		<div class="error">
			Unable to send invite.
			<s:bind path="invitees">
		  	<c:if test="${status.error && status.errorCode == 'typeMismatch'}">
		  	Please ensure your invitees have been entered using the correct format and resubmit.
		  	<c:set var="errorDisplayed" value="true" />
		  	</c:if>
 			</s:bind>
 			<c:if test="${!errorDisplayed}">
		  	All fields are required.
 			</c:if>
 		</div>
	  	</c:if>
  		</s:bind>  		
  		<p>Send a personal Greenhouse invitation to your friends and colleagues.</p>  		
	</div>
	<fieldset>
		<legend>Tell us who you'd like to send an invite to</legend>
		<form:label path="invitees">Invitees, separated by commas in format <i>firstName lastName &lt;email&gt;</i></form:label>
		<form:textarea path="invitees" cols="60" />
	</fieldset>
	<fieldset>
		<legend>Personalize your invitation</legend>
		<form:label path="invitationText">Invitation Text</form:label>
		<form:textarea path="invitationText" cols="60" rows="10" />
	</fieldset>
	<p><button type="submit">Send</button></p>	
</form:form>