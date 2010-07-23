<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page session="false" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:fb="http://www.facebook.com/2008/fbml">
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
	<script type="text/javascript" src="<c:url value="/resources/jquery/jquery-1.4.2.js" />"></script>	
</head>
<body>
    <%-- Facebook advises placing this in the body, not the header  --%>
<div id="fb-root"></div>
<script src="http://connect.facebook.net/en_US/all.js"></script>
<script>
  FB.init({appId: '8f007e7ce33d82dc2f5485102b3504c2', status: true, cookie: true, xfbml: true});
  FB.Event.subscribe('auth.sessionChange', function(response) {
    if (response.session) {
     //   alert("Hey!");
    } else {
     //   alert("See ya!");
    }
  });
</script>
	
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
//    FB.init({apiKey: '8f007e7ce33d82dc2f5485102b3504c2', status: true, cookie: true,            xfbml: true});

//        FB.init("8f007e7ce33d82dc2f5485102b3504c2", "<c:url value="/resources/xd_receiver.html" />", {appId: '140372495981006', status: true, cookie: true, xfbml: true});
//        FB.init({appId: '140372495981006', status: true, cookie: true, xfbml: true});
    </script>
        	
</body>
</html>