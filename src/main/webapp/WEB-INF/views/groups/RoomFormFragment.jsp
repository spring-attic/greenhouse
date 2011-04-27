<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>

<h2>Add a room </h2>

<fieldset>

<form:label path="name">Name </form:label>
		<form:input path="name" />
		
<form:label path ="capacity">Capacity </form:label>
		<form:input path="capacity" />
		
<form:label path ="locationHint">Location Hint </form:label>
		<form:input path="locationHint" />
		
<p><button type="submit">Add a Room</button></p>
			
</fieldset>