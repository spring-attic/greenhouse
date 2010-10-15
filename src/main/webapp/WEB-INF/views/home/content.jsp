<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertAttribute name="intro" />

<div id="recentActivity">
	<h3>Recent Activity</h3>
	<tiles:insertAttribute name="recentActivity" />
</div>