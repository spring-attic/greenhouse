<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>

<form:label path="venueID">Select Venue</form:label>
		<form:select path="venueID" onchange='onChange(this.form.venueID);'>
				<form:option value="">Add New Venue</form:option>
					<c:forEach var="venues" items="${venueList}" varStatus="status">
				<form:option value="${status.index+1}">${venues}</form:option>
					</c:forEach>
		</form:select> 
		<script type="text/javascript">
			var addList = new Array();
			var hintList = new Array();
			var venueList = new Array();
		</script>
		
		<c:forEach var="venues" items="${venueList}" varStatus="status">
			<script type="text/javascript">
				venueList["${status.index+1}"]="${venues}";
			</script>
		</c:forEach>
		
		<c:forEach var="addresses" items="${addressList}" varStatus="status">
			<script type="text/javascript">
				addList["${status.index}"]="${addresses}";
			</script>
		</c:forEach>
		
		<c:forEach var="hints" items="${hintsList}" varStatus="status">
			<script type="text/javascript">
				hintList["${status.index}"]="${hints}";
			</script>
		</c:forEach>
		
		<form:label path="venueName">Venue Name <form:errors path="venueName" cssClass="error" /></form:label>
		<form:input path="venueName" />
		<form:label path="venueAddress">Venue Address <form:errors path="venueAddress" cssClass="error" /></form:label>
		<form:input path="venueAddress" />
		<form:label path="locationHint">Location Hint(optional) <form:errors path="locationHint" cssClass="error" /></form:label>
		<form:input path="locationHint" />
			
		<script type="text/javascript">

			function onChange(dropdown)
			{	
				var index = dropdown.selectedIndex;
				if (index != 0){
					$("#venueName").val(venueList[index]);
				} else {
					$("#venueName").val("");
				}
				if (index != 0){
					$("#venueAddress").val(addList[index-1]);
				} else {
					$("#venueAddress").val("");
				}
				if (index != 0){
					$("#locationHint").val(hintList[index-1]);
				} else {
					$("#locationHint").val("");
				}
				if (index != 0) {
					$("#venueName").attr("readonly", true);
					$("#venueAddress").attr("readonly", true);
					$("#locationHint").attr("readonly", true);
					} else {
						$("#venueName").attr("readonly", false);
						$("#venueAddress").attr("readonly", false);
						$("#locationHint").attr("readonly", false);
					}

			}
		</script>