<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>

<h2>Invite friends to the Greenhouse</h2>
<c:if test="${not empty message}">
	<div class="${message.type}">${message.text}</div>
</c:if>
		
<ul class="listings">
	<li class="listing">	
		<img src="<c:url value="/resources/social/twitter/logo.png" />" alt="Twitter Connect Logo" />
		See which of your <a href="<c:url value="/invite/twitter" />">Twitter followers</a> are in the Greenhouse
	</li> 
	<li class="listing">
		<img src="<c:url value="/resources/social/facebook/connect_light_medium_short.gif" />" alt="Facebook Connect Logo" id="fb_login_image" />		
		Invite your <a class="button large" href="#" onclick="FB.requireSessionThenGoTo('<c:url value="/invite/facebook"/>');return false;">Facebook friends</a>
		  <c:if test="${not empty facebookUserId}">
          (you are currently logged into Facebook as <fb:name linked="false" useyou="false" uid="${facebookUserId}"></fb:name>.
          <a onclick="FB.logoutThenGoTo('<c:url value="/signout"/>');return false;" href="#">Not you?</a>)
          </c:if>          
	</li>
	<li class="listing">
		<img src="<c:url value="/resources/social/google/logo-gmail.png" />" alt="Gmail Logo "/>
		Invite friends via <a href="<c:url value="/invite/mail" />">email</a>
	</li>	
</ul>

<facebook:init />