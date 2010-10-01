<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html>
<html>
<head>
	<title>
		<tiles:insertAttribute name="title" defaultValue="Greenhouse" />
	</title>
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
	<link rel="stylesheet" href="<c:url value="/resources/page.css" />" type="text/css" media="screen" />
	<tiles:useAttribute id="styles" name="styles" classname="java.util.List" ignore="true" />
	<c:forEach var="style" items="${styles}">
		<link rel="stylesheet" href="<c:url value="/resources/${style}" />" type="text/css" media="all" />
	</c:forEach>
	<c:forEach var="meta" items="${metadata}">
		<meta name="${meta.key}" content="${meta.value}"/> 
	</c:forEach>
	<script>
		var greenhouse = greenhouse || {};
		greenhouse.contextPath = "${pageContext.request.contextPath}";
	</script>
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.4/jquery.js" />"></script>	
	<script type="text/javascript" src="<c:url value="/resources/jquery-cookie/1.0/jquery-cookie.js" />"></script>
</head>
<body>
	<div id="header">
		<tiles:insertAttribute name="header" />
	</div>
	<div id="content">
		<tiles:insertAttribute name="content" />
	</div>
	<div id="footer">
		Copyright (c) 2010 SpringSource
	</div>
	<tiles:useAttribute id="scripts" name="scripts" classname="java.util.List" ignore="true" />
	<c:forEach var="script" items="${scripts}">
		<script type="text/javascript" src="<c:url value="/resources/${script}" />"></script>	
	</c:forEach>
	<script type="text/javascript">
		$.cookie('Greenhouse.timeZoneOffset', new Date().getTimezoneOffset() / 60 * -1);
	</script>
</body>
</html>