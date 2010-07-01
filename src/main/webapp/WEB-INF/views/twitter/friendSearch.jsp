<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form id="twitterFriendFinder" action="<c:url value="/import/twitter" />" method="post">
	<div class="header">
  		<h2>Find Twitter Friends in the Greenhouse</h2>
	</div>	
  	<fieldset>
		<label for="twitterName">Twitter username:</label>
		<input id="twitterName" name="twitterName" type="text" size="25" autocorrect="off" autocapitalize="off" value="${twitterName}"}/>
	</fieldset>
	<input type="submit" value="Go"/>
</form>