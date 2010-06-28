<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        
<c:if test="${!empty sessionScope.OAUTH_FAILURE_KEY}">
    <div class="error">
    	<c:out value="${message}"/>
    </div>    
    <code>
    	<c:out value="${stackTrace}"/>
    </code>
</c:if>
<c:remove scope="session" var="OAUTH_FAILURE_KEY"/>