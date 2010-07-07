<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form id="friendFinder" action="<c:url value="/invites/twitter" />" method="post">
	<div class="header">
  		<h2>Find Twitter friends that are members of the Greenhouse</h2>
	</div>
  	<fieldset>
		<label for="username">Twitter Username:</label>
		<input id="username" name="username" type="text" size="25" autocorrect="off" autocapitalize="off" value="${username}" />
	</fieldset>
	<input type="submit" value="Go"/>
</form>