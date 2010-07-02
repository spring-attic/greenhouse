<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        
<div class="error">
	<p>There was a problem communicating with Twitter: <c:out value="${error}"/></p>
	<p>See <a href="http://status.twitter.com/">Twitter's Status Page</a> for more information.</p>
</div>    
<code>
	<c:out value="${stackTrace}"/>
</code>
