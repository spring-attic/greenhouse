<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${not empty message}">
	<div class="${message.type}">${message.text}</div>
</c:if>

<form id="resetPassword" method="post">
	<div class="formInfo">
  		<h2>Reset Password</h2>
	</div>
  	<fieldset>
  		<label for="username">
  			Username or Email <c:if test="${username.error}"><span class="error">${username.errorMessage}</span></c:if>
  		</label>
  		<input id="username" type="text" name="username" value="${username.value}" />
	</fieldset>
	<p>
		<button type="submit">Submit</button>
	</p>
</form>