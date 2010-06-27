<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<p>${connectedMessage}</p>

<form id="disconnect" method="post">
	<p>Your Greenhouse account is currently linked to your Twitter account</p>
	<input type="submit" value="Disconnect" />
	<input type="hidden" name="_method" value="DELETE" />
</form>