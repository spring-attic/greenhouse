<%@ page session="false" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertAttribute name="intro" />

<div id="recentActivity">
	<h2>Recent Activity</h2>
	<tiles:insertAttribute name="recentActivity" />
</div>