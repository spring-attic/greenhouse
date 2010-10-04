<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
	<fieldset>
		<form:label path="name">Name <form:errors path="name" cssClass="error" /></form:label>
		<form:input path="name" />
		<form:label path="description">Description <form:errors path="description" cssClass="error" /></form:label>
		<form:input path="description" />
		<form:label path="organization">Organization</form:label>
		<form:input path="organization" />
		<form:label path="website">Website</form:label>  
		<form:input path="website" />
		<form:label path="callbackUrl">Callback URL</form:label>
		<form:input path="callbackUrl" />
	</fieldset>