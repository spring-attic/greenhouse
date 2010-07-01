<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<h2>Greenhouse Updates</h2>

<div id="updates">
	<ul>
		<c:forEach var="update" items="${updates}">
			<li>
				${update.text} <br /> 
				${update.timestamp}
			</li>
		</c:forEach>
	</ul>
</div>
