<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<h4>Invite friends to the Greenhouse</h4>

<ul class="inviteNetworks">
	<li class="inviteNetwork">
		<img src="<c:url value="/resources/social/twitter/logo.png" />" alt="Twitter Logo"/> <a href="<c:url value="/invite/twitter"/>">Find the friends you follow on Twitter</a>
	</li>
	<li class="inviteNetwork">
		<img src="http://static.ak.fbcdn.net/images/fbconnect/login-buttons/connect_light_medium_short.gif" alt="Facebook Connect" width="89" height="21" id="fb_login_image" />
		<a href="<c:url value="/invite/facebook"/>">Find your friends on Facebook</a>
          (you are currently logged into Facebook as <fb:name linked="false" useyou="false" uid="${facebookUserId}"></fb:name>.
          <a onclick="FB.Connect.logoutAndRedirect('<c:url value="/signout"/>'); return false;" href="#">Not you?</a>)  
	</li>
</ul>
