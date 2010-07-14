<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form id="resetPassword" method="post">
	<div class="header">
  		<h2>Reset Password</h2>
		<c:if test="${not empty message}">
			<div class="${message.type}">${message.text}</div>
		</c:if>
	</div>
  	<fieldset>
  		<label for="username">
  			Username or Email <c:if test="${username.error}"><span class="fieldError">${username.errorMessage}</span></c:if>
  		</label>
  		<input id="username" type="text" name="username" value="${username.value}" />
	</fieldset>
	<input type="submit" value="Submit">
</form>
