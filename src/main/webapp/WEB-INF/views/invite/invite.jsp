<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>

<h2>Invite friends to the Greenhouse</h2>

<c:if test="${not empty message}">
<div class="${message.type.cssClass}">${message.text}</div>
</c:if>
		
<ul class="listings">
	<li class="listing">	
		<img src="<c:url value="/resources/social/twitter/logo.png" />" alt="Twitter Connect Logo"  style="max-height: 43px; max-width: 88px;" />
		See which of your <a href="<c:url value="/invite/twitter" />">Twitter followers</a> are in the Greenhouse
	</li> 
	<li class="listing">
		<img src="<c:url value="/resources/social/facebook/connect_light_medium_short.gif" />" alt="Facebook Connect Logo" id="fb_login_image" style="max-height: 43px; max-width: 88px;"/>		
		Invite your <a class="button large" href="<c:url value="/invite/facebook"/>" onclick="FB.requireSessionThenGoTo('<c:url value="/invite/facebook"/>');return false;">Facebook friends</a>
		  <c:if test="${not empty facebookUserId}">
          (you are currently logged into Facebook as <fb:name linked="false" useyou="false" uid="${facebookUserId}"></fb:name>.
          <a onclick="FB.logoutThenGoTo('<c:url value="/signout"/>');return false;" href="#">Not you?</a>)
          </c:if>          
	</li>
	<li class="listing">
		<img src="<c:url value="/resources/social/google/logo-gmail.png" />" alt="Gmail Logo " style="max-height: 43px; max-width:88px;" />
		Invite friends via <a href="<c:url value="/invite/mail" />">email</a>
	</li>	
</ul>

<facebook:init appId="${facebookAppId}" />
<script>
if(FB) {
	FB.requireSessionThenGoTo = function(url) {
		FB.getLoginStatus(function(response) {
			if (response.session) { 
				window.location = url; 
			} else {
				FB.login(function(response) {
					if (response.session) {
						window.location = url;
					}
				});
			}
		});
	};
	
	FB.logoutThenGoTo = function(url) {
		FB.logout(function(response) { 
			window.location = url; 
		});
	};
}
</script>
