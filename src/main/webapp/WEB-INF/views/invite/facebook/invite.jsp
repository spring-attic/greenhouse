<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<div>
<fb:serverfbml style="width: 625px;">
	<script type="text/fbml">
		<fb:fbml>
			<fb:request-form action="<s:url value="/invite/facebook" />?skip=1" method="POST" invite="true" type="Greenhouse" content="Join me in The Greenhouse! It's the best way to exchange Spring ideas and information with other developers. <fb:req-choice url='http://greenhouse.springsource.org' label='Enter the Greenhouse' />">
				<fb:multi-friend-selector bypass="cancel" email_invite="false" import_external_friends="false" cols="4" showborder="false" actiontext="Invite your Facebook friends to enter the Greenhouse"></fb:multi-friend-selector>
			</fb:request-form>
		</fb:fbml>
	</script>
</fb:serverfbml>
</div>        
