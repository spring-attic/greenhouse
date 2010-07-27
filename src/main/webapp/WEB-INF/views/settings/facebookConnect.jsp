<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form id="fb_signin" action="<c:url value="/settings/facebook" />" method="post">
	<fb:login-button perms="email,offline_access" onlogin="$('#fb_signin').submit();" v="2" length="long">Connect to Facebook</fb:login-button>
</form>
