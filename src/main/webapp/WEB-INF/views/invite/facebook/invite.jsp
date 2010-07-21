<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div>
<fb:serverfbml style="width: 625px;">
	<script type="text/fbml">
		<fb:fbml>
			<fb:request-form action="<c:url value="http://localhost:8080/greenhouse/invite/facebook"/>" method="POST" invite="true" type="Greenhouse" content="Join me in The Greenhouse! It's the best way to exchange Spring ideas and information with other developers. <fb:req-choice url='http://www.springsource.com' label='Enter the Greenhouse' />">
				<fb:multi-friend-selector bypass="cancel" cols="4" showborder="false" actiontext="Invite your Facebook friends to enter the Greenhouse"></fb:multi-friend-selector>
			</fb:request-form>
		</fb:fbml>
	</script>
</fb:serverfbml>
</div>        
