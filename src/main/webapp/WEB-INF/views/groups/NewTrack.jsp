<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:url var="actionUrl" value="/groups/NewTrack"/>
<form:form action="${actionUrl}" method="post" modelAttribute="eventTrackForm">
<fieldset>
	<form:label path="name">Name </form:label>
		<form:input path="name" />
	<form:label path="code">Code </form:label>
		<form:input path="code" />
		
		<form:label path="description">Description</form:label>
		<form:textarea cols="55" rows="8" path="description" />
		
		
		</fieldset>
	<p><button type="submit">Add Track</button></p>	
</form:form>
