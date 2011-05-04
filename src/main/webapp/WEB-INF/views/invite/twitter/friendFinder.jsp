<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form id="friendFinder" action="<c:url value="/invite/twitter" />" method="post">
	<div class="formInfo">
		<h2>Find Twitter Friends</h2>
  		<p>See who you follow is also a member of the Greenhouse.</p>
	</div>
  	<fieldset>
		<label for="username">Twitter Username</label>
		<input id="username" name="username" type="text" size="25" value="${username}" />
	</fieldset>
	<p>
		<button type="submit">Find</button>
	</p>
</form>

<!-- TODO consider using load here; apply a spinner effect; make progressive -->
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