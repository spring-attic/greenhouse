<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>

<h2>Accept Invite</h2>
<div class="info">
Accept your Greenhouse invitation from <a href="<c:url value="/members/${sentBy.id}" />">${sentBy.label}</a> by completing the following form.
</div>

