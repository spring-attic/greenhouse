<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form id="friendFinder" action="<c:url value="/invite/twitter" />" method="post">
	<div class="header">
  		<h2>See who you follow is also a member of the Greenhouse</h2>
	</div>
  	<fieldset>
		<label for="username">Twitter Username</label>
		<input id="username" name="username" type="text" size="25" autocorrect="off" autocapitalize="off" value="${username}" />
	</fieldset>
	<input type="submit" value="Find"/>
</form>

<script type="text/javascript">
$(document).ready(function() {
	$("#friendFinder").submit(function() {
		$.post(this.action, $(this).serialize(), function(friends) {
			$("#friends").html(friends);
		});		
		return false;
	});
});
</script>

<div id="friends">
</div>