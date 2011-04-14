<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:url var="actionUrl" value="/groups/NewSession"/>
<form:form action="${actionUrl}" method="post" modelAttribute="eventSessionForm">


<jsp:include page="SessionFormFragment.jsp" />
</form:form>
