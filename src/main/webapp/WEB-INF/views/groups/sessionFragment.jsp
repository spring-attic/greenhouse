<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>

<form:label path="leaderID">Select Speaker </form:label>
		<form:select path="leaderID" onchange='onChange(this.form.leaderID);'>
				<form:option value="">Add Leader</form:option>
					<c:forEach var="speaker" items="${speakerList}" varStatus="status">
				<form:option value="${status.index+1}">${speaker}</form:option>
					</c:forEach>
	
					
					
		</form:select> 
		<form:label path="name">Leader Name<form:errors path="name" cssClass="error"/></form:label>
		<form:input path="name"/>
		<form:label path="company">Company<form:errors path="company" cssClass="error"/></form:label>
		<form:input path="company"/>
		<form:label path="companyTitle">Title<form:errors path="companyTitle" cssClass="error"/></form:label>
		<form:input path="companyTitle"/>
		<form:label path="companyURL">Company URL<form:errors path="companyURL" cssClass="error"/></form:label>
		<form:input path="companyURL"/>
		<form:label path="twitterName">Twitter Username<form:errors path="twitterName" cssClass="error"/></form:label>
		<form:input path="twitterName"/>
		
		
		<script type="text/javascript">
		function onChange(dropdown){
			var index = dropdown.selectedIndex;
		if (index != 0) {
					$("#name").attr("disabled",true);
					$("#company").attr("disabled", true);
					$("#companyTitle").attr("disabled", true);
					$("#companyURL").attr("disabled" , true);
					$("#twitterName").attr("disabled", true);
					} else {
						$("#name").attr("disabled", false);
						$("#company").attr("disabled", false);
						$("#companyTitle").attr("disabled", false);
						$("#companyURL").attr("disabled" , false);
						$("#twitterName").attr("disabled", false);
					}}
		</script>


<p><button type="submit">Add New Session</button></p>
