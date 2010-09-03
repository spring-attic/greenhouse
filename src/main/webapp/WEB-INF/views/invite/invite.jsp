<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>

<div class="header">
	<h2>Invite friends to the Greenhouse</h2>
	<c:if test="${not empty message}">
		<div class="${message.type}">${message.text}</div>
	</c:if>
</div>
		
<ul id="inviteNetworks">
	<li>
		<img src="<c:url value="/resources/social/twitter/logo.png" />" alt="Twitter Logo"/>
		<a href="<c:url value="/invite/twitter"/>">Find the friends you follow on Twitter</a>
	</li>
	<li>
		<img src="http://static.ak.fbcdn.net/images/fbconnect/login-buttons/connect_light_medium_short.gif" alt="Facebook Connect" id="fb_login_image" />		
		<a class="button large" href="#" onclick="FB.requireSessionThenGoTo('<c:url value="/invite/facebook"/>');return false;">Find your friends on Facebook</a>
		  <c:if test="${not empty facebookUserId}">
          (you are currently logged into Facebook as <fb:name linked="false" useyou="false" uid="${facebookUserId}"></fb:name>.
          <a onclick="FB.logoutThenGoTo('<c:url value="/signout"/>');return false;" href="#">Not you?</a>)
          </c:if>          
	</li>
</ul>

<s:eval expression="@apiProvider.apiKey" var="apiKey" />
<facebook:init apiKey="${apiKey}" />
