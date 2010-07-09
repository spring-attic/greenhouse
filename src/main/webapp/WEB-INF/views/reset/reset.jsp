<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form id="resetPassword" method="post">
	<div class="header">
  		<h2>Reset Password</h2>
		<c:if test="${not empty successMessage}">
			<div class="success">${successMessage}</div>
		</c:if>
	</div>
  	<fieldset>
  		<label for="username">
  			Username or Email <c:if test="${error}"><span class="fieldError">${errorMessage}</span></c:if>
  		</label>
  		<input id="username" type="text" name="username" />
	</fieldset>
	<input type="submit" value="Submit">
</form>
