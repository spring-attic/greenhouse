<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html>
<html>
<head>
	<title><tiles:insertAttribute name="title" defaultValue="Greenhouse" /></title>
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
	<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;" />
	<link rel="stylesheet" href="<c:url value="/resources/mobile/page.css" />" type="text/css" media="screen" />
	<tiles:useAttribute id="styles" name="styles" classname="java.util.List" ignore="true" />
	<c:forEach var="style" items="${styles}">
		<link rel="stylesheet" href="<c:url value="/resources/${style}" />" type="text/css" media="all" />
	</c:forEach>	
	<script type="text/javascript" src="<c:url value="/resources/jquery/jquery-1.4.2.js" />"></script>
	<script type="text/javascript">
		window.scrollTo(0, 1);
	</script>
</head>
<body>
	<div id="header">
		<tiles:insertAttribute name="header" />
	</div>
	<div id="content">
		<tiles:insertAttribute name="content" />
	</div>
	<div id="footer">
		<tiles:insertAttribute name="footer" />	
	</div>
	<tiles:useAttribute id="scripts" name="scripts" classname="java.util.List" ignore="true" />
	<c:forEach var="script" items="${scripts}">
		<script type="text/javascript" src="<c:url value="/resources/${script}" />"></script>	
	</c:forEach>
</body>
</html>