<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<h2>Find Results</h2>
<c:choose>
	<c:when test="${not empty friendAccounts}">
		<ul>
			<c:forEach var="friendAccount" items="${friendAccounts}">
				<li>
					<a href="<s:url value="/members/${friendAccount.username}" />">${friendAccount.fullName}</a>
				</li>
			</c:forEach>
		</ul>
	</c:when>
	<c:when test="${empty friendAccounts}">
		<p>No friends found</p>
	</c:when>
</c:choose>
