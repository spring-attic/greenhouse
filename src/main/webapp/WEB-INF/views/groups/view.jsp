<%@ page session="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/spring-social/facebook/tags" prefix="facebook" %>

<h2><c:out value="${group.name}" /></h2>
<p><c:out value="${group.description}" escapeXml="true" /></p>

<div id="fb-root"></div>
<fb:like></fb:like>

<facebook:init appId="${facebookAppId}" />